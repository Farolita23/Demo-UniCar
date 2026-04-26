package com.daw.datamodel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.User;

/**
 * Repositorio de acceso a datos para la entidad {@link User}.
 *
 * Proporciona consultas derivadas para verificar unicidad de credenciales,
 * recuperar usuarios por nombre de usuario y obtener usuarios relacionados con
 * entidades del dominio (vehículos, valoraciones, reportes). También incluye
 * una búsqueda flexible por nombre o nombre de usuario.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Comprueba si existe un usuario con el nombre de usuario indicado.
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si ya está en uso, {@code false} en caso contrario
     */
    boolean existsByUsername(String username);

    /**
     * Comprueba si existe un usuario con el correo electrónico indicado.
     *
     * @param email dirección de correo a verificar
     * @return {@code true} si ya está registrada, {@code false} en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return {@link Optional} con el usuario si existe, vacío en caso contrario
     */
    Optional<User> findByUsername(String username);

    /**
     * Recupera el propietario de un vehículo a partir del identificador del vehículo.
     *
     * @param idCar identificador del vehículo
     * @return usuario propietario del vehículo
     */
    User findByCarsId(Long idCar);

    /**
     * Recupera el usuario que ha recibido una valoración concreta.
     *
     * @param idRating identificador de la valoración
     * @return usuario receptor de la valoración
     */
    User findByRatingsReceivedId(Long idRating);

    /**
     * Recupera el usuario que ha emitido una valoración concreta.
     *
     * @param idRating identificador de la valoración
     * @return usuario emisor de la valoración
     */
    User findByRatingsDoneId(Long idRating);

    /**
     * Recupera el usuario que ha recibido un reporte concreto.
     *
     * @param idReport identificador del reporte
     * @return usuario reportado
     */
    User findByReportsReceivedId(Long idReport);

    /**
     * Recupera el usuario que ha emitido un reporte concreto.
     *
     * @param idReport identificador del reporte
     * @return usuario que emitió el reporte
     */
    User findByReportsDoneId(Long idReport);

    /**
     * Busca usuarios cuyo nombre o nombre de usuario contenga el término indicado,
     * sin distinguir entre mayúsculas y minúsculas.
     *
     * @param query término de búsqueda parcial
     * @return lista de usuarios coincidentes; vacía si no hay resultados
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchByNameOrUsername(@Param("q") String query);

}
