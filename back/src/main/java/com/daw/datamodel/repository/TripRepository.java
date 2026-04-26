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
import com.daw.datamodel.entities.User;

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
     * @param user     entidad del usuario para calcular afinidad por historial
     * @param pageable configuración de paginación y ordenación
     * @return página de viajes recomendados
     */
    @Query("SELECT t FROM Trip t WHERE " +
               "t.campus.id = :campusId AND t.town.id = :townId " +
               "AND t.departureDate >= CURRENT_DATE " +
               "ORDER BY " +
               "(SELECT COUNT(pt) FROM Trip pt " +
               " WHERE :user MEMBER OF pt.passengers " +
               " AND pt.car.driver = t.car.driver) DESC, " +
               "t.departureDate ASC, t.departureTime ASC")
    Page<Trip> findRecommendedTrips(@Param("campusId") Long campusId,
                                    @Param("townId") Long townId,
                                    @Param("user") User user,
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
    @EntityGraph(attributePaths = {"passengers", "requesters", "car", "campus", "town"})
    @Query("SELECT t FROM Trip t WHERE t.car.driver.id = :idDriver " +
           "ORDER BY t.departureDate DESC, t.departureTime DESC")
    Page<Trip> findTripsAsADriver(@Param("idDriver") Long idDriver, Pageable pageable);

    /**
     * Recupera los viajes en los que un usuario participa como pasajero confirmado,
     * ordenados por fecha y hora de salida descendente.
     *
     * @param idPassenger identificador del pasajero
     * @param pageable    configuración de paginación
     * @return página de viajes como pasajero
     */
    @Query("SELECT t FROM Trip t JOIN t.passengers p WHERE p.id = :idPassenger " +
           "ORDER BY t.departureDate DESC, t.departureTime DESC")
    Page<Trip> findTripsAsAPassenger(@Param("idPassenger") Long idPassenger, Pageable pageable);

    /**
     * Recupera los viajes cuya fecha de salida es igual o posterior a la fecha actual,
     * ordenados de más próximo a más lejano.
     *
     * @param pageable configuración de paginación
     * @return página de viajes futuros disponibles
     */
    @Query("SELECT t FROM Trip t WHERE t.departureDate >= CURRENT_DATE " +
           "ORDER BY t.departureDate ASC, t.departureTime ASC")
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
    @Query("SELECT DISTINCT t FROM Trip t WHERE t.departureDate >= CURRENT_DATE " +
           "AND (t.campus.id IN :campusIds OR t.town.id IN :townIds) " +
           "ORDER BY t.departureDate ASC, t.departureTime ASC")
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

}
