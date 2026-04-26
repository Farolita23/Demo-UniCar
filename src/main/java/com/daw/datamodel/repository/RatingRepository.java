package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Rating;

/**
 * Repositorio de acceso a datos para la entidad {@link Rating}.
 *
 * Además de las operaciones CRUD estándar, expone una consulta de existencia
 * que garantiza la restricción de una única valoración por par de usuarios.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Rating
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Comprueba si ya existe una valoración emitida por un usuario concreto hacia otro.
     *
     * @param userRateId  identificador del usuario que emite la valoración
     * @param ratedUserId identificador del usuario receptor de la valoración
     * @return {@code true} si ya existe la valoración, {@code false} en caso contrario
     */
    boolean existsByUserRateIdAndRatedUserId(Long userRateId, Long ratedUserId);

}
