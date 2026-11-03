package com.daw.datamodel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Trip;

/**
 * Repositorio de acceso a datos para la entidad {@link Trip}.
 *
 * Implementa {@link JpaSpecificationExecutor} para soportar búsquedas dinámicas
 * a través de {@link TripSpecification}. Contiene consultas JPQL personalizadas para
 * los casos de uso más complejos: recomendaciones personalizadas, historial como
 * conductor o pasajero, viajes sugeridos y búsqueda de IDs para sugerencias.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Trip
 * @see TripSpecification
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

    /**
     * Comprueba si dos usuarios han compartido algún viaje juntos (como conductor y pasajero,
     * o ambos como pasajeros en el mismo viaje).
     *
     * @param idUser1 identificador del primer usuario
     * @param idUser2 identificador del segundo usuario
     * @return {@code true} si existe al menos un viaje compartido, {@code false} en caso contrario
     */
    @Query("SELECT COUNT(t) > 0 FROM Trip t JOIN t.passengers p " +
           "WHERE (t.car.driver.id = :idUser1 AND p.id = :idUser2) " +
           "OR (t.car.driver.id = :idUser2 AND p.id = :idUser1) " +
           "OR (EXISTS (SELECT 1 FROM Trip t2 JOIN t2.passengers p1 JOIN t2.passengers p2 " +
           "WHERE p1.id = :idUser1 AND p2.id = :idUser2))")
    boolean existsSharedTrip(@Param("idUser1") Long idUser1, @Param("idUser2") Long idUser2);

    /**
     * Recupera viajes futuros recomendados para un usuario, ordenados por afinidad con
     * conductores previos y posteriormente por fecha y hora de salida.
     *
     * @param campusId identificador del campus habitual del usuario
     * @param townId   identificador de la localidad habitual del usuario
     * @param userId   identificador del usuario para calcular afinidad por historial
     * @param pageable configuración de paginación y ordenación
     * @return página de viajes recomendados
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query(value = "SELECT t FROM Trip t WHERE " +
               "t.campus.id = :campusId AND t.town.id = :townId " +
               "AND t.departureDate >= CURRENT_DATE " +
               "ORDER BY " +
               "(SELECT COUNT(pt) FROM Trip pt JOIN pt.passengers pp " +
               " WHERE pp.id = :userId " +
               " AND pt.car.driver.id = t.car.driver.id) DESC, " +
               "t.departureDate ASC, t.departureTime ASC, t.id ASC",
           countQuery = "SELECT COUNT(t) FROM Trip t WHERE " +
               "t.campus.id = :campusId AND t.town.id = :townId " +
               "AND t.departureDate >= CURRENT_DATE")
    Page<Trip> findRecommendedTrips(@Param("campusId") Long campusId,
                                    @Param("townId") Long townId,
                                    @Param("userId") Long userId,
                                    Pageable pageable);

    /**
     * Recupera los viajes en los que un usuario actúa como conductor, ordenados
     * por fecha y hora de salida descendente. Usa {@code @EntityGraph} para
     * inicializar las colecciones {@code passengers} y {@code requesters} en
     * una sola consulta y evitar el problema N+1.
     *
     * @param idDriver identificador del conductor
     * @param pageable configuración de paginación
     * @return página de viajes como conductor
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query("SELECT t FROM Trip t WHERE t.car.driver.id = :idDriver AND t.departureDate > CURRENT_DATE " +
           "ORDER BY t.departureDate DESC, t.departureTime DESC, t.id DESC")
    Page<Trip> findTripsAsADriver(@Param("idDriver") Long idDriver, Pageable pageable);

    /**
     * Recupera los viajes en los que un usuario participa como pasajero confirmado,
     * ordenados por fecha y hora de salida descendente.
     *
     * @param idPassenger identificador del pasajero
     * @param pageable    configuración de paginación
     * @return página de viajes como pasajero
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query(value = "SELECT t FROM Trip t JOIN t.passengers p WHERE p.id = :idPassenger " +
           "ORDER BY t.departureDate DESC, t.departureTime DESC, t.id DESC",
           countQuery = "SELECT COUNT(t) FROM Trip t JOIN t.passengers p WHERE p.id = :idPassenger")
    Page<Trip> findTripsAsAPassenger(@Param("idPassenger") Long idPassenger, Pageable pageable);

    /**
     * Recupera los viajes cuya fecha de salida es igual o posterior a la fecha actual,
     * ordenados de más próximo a más lejano.
     *
     * @param pageable configuración de paginación
     * @return página de viajes futuros disponibles
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query(value = "SELECT t FROM Trip t WHERE t.departureDate >= CURRENT_DATE " +
           "ORDER BY t.departureDate ASC, t.departureTime ASC, t.id ASC",
           countQuery = "SELECT COUNT(t) FROM Trip t WHERE t.departureDate >= CURRENT_DATE")
    Page<Trip> findFutureTrips(Pageable pageable);

    /**
     * Recupera viajes futuros asociados a los campus o localidades indicados,
     * usados para la funcionalidad de sugerencias personalizadas.
     *
     * @param campusIds lista de identificadores de campus de interés
     * @param townIds   lista de identificadores de localidades de interés
     * @param pageable  configuración de paginación
     * @return página de viajes sugeridos
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query(value = "SELECT t FROM Trip t WHERE t.departureDate >= CURRENT_DATE " +
           "AND (t.campus.id IN :campusIds OR t.town.id IN :townIds) " +
           "ORDER BY t.departureDate ASC, t.departureTime ASC, t.id ASC",
           countQuery = "SELECT COUNT(t) FROM Trip t WHERE t.departureDate >= CURRENT_DATE " +
           "AND (t.campus.id IN :campusIds OR t.town.id IN :townIds)")
    Page<Trip> findSuggestedTrips(@Param("campusIds") List<Long> campusIds,
                                   @Param("townIds") List<Long> townIds,
                                   Pageable pageable);

    /**
     * Obtiene los identificadores de campus de los viajes en los que el usuario ha viajado como pasajero.
     *
     * @param userId identificador del usuario
     * @return lista de IDs de campus únicos
     */
    @Query("SELECT DISTINCT t.campus.id FROM Trip t JOIN t.passengers p WHERE p.id = :userId")
    List<Long> findCampusIdsByPassenger(@Param("userId") Long userId);

    /**
     * Obtiene los identificadores de localidades de los viajes en los que el usuario ha viajado como pasajero.
     *
     * @param userId identificador del usuario
     * @return lista de IDs de localidades únicos
     */
    @Query("SELECT DISTINCT t.town.id FROM Trip t JOIN t.passengers p WHERE p.id = :userId")
    List<Long> findTownIdsByPassenger(@Param("userId") Long userId);

    /**
     * Obtiene los identificadores de campus de los viajes en los que el usuario ha solicitado plaza.
     *
     * @param userId identificador del usuario
     * @return lista de IDs de campus únicos
     */
    @Query("SELECT DISTINCT t.campus.id FROM Trip t JOIN t.requesters r WHERE r.id = :userId")
    List<Long> findCampusIdsByRequester(@Param("userId") Long userId);

    /**
     * Obtiene los identificadores de localidades de los viajes en los que el usuario ha solicitado plaza.
     *
     * @param userId identificador del usuario
     * @return lista de IDs de localidades únicos
     */
    @Query("SELECT DISTINCT t.town.id FROM Trip t JOIN t.requesters r WHERE r.id = :userId")
    List<Long> findTownIdsByRequester(@Param("userId") Long userId);

    /**
     * Recupera todos los viajes generados a partir de un viaje periódico.
     *
     * @param periodicTripId identificador del viaje periódico
     * @param pageable configuración de paginación
     * @return página de viajes generados
     */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    @Query(value = "SELECT t FROM Trip t WHERE t.periodicTrip.id = :periodicTripId " +
           "ORDER BY t.departureDate ASC, t.departureTime ASC, t.id ASC",
           countQuery = "SELECT COUNT(t) FROM Trip t WHERE t.periodicTrip.id = :periodicTripId")
    Page<Trip> findByPeriodicTripId(@Param("periodicTripId") Long periodicTripId, Pageable pageable);

    /**
     * Cuenta los viajes individuales generados a partir de un viaje periódico.
     *
     * @param periodicTripId identificador del viaje periódico
     * @return número de viajes vinculados a esa plantilla
     */
    long countByPeriodicTripId(Long periodicTripId);

    /**
     * Cuenta, en una sola consulta, los viajes generados por cada plantilla periódica indicada.
     *
     * @param ids identificadores de las plantillas
     * @return filas {@code [periodicTripId, total]} (las plantillas sin viajes no aparecen)
     */
    @Query("SELECT t.periodicTrip.id, COUNT(t) FROM Trip t WHERE t.periodicTrip.id IN :ids GROUP BY t.periodicTrip.id")
    List<Object[]> countByPeriodicTripIds(@Param("ids") java.util.Collection<Long> ids);

    /** Carga un viaje con su coche, conductor, campus y localidad en una sola consulta. */
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    java.util.Optional<Trip> findDetailedById(Long id);

    @Override
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    Page<Trip> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"car", "car.driver", "campus", "town"})
    Page<Trip> findAll(org.springframework.data.jpa.domain.Specification<Trip> spec, Pageable pageable);

}
