package com.daw.datamodel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Favorite;

/**
 * Repositorio de acceso a datos para la entidad {@link Favorite}.
 *
 * Proporciona consultas para gestionar las relaciones de favoritos entre usuarios,
 * incluyendo búsqueda, comprobación de existencia y eliminación por par de usuarios.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see Favorite
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * Recupera todos los favoritos marcados por un usuario concreto.
     *
     * @param userId identificador del usuario que tiene los favoritos
     * @return lista de relaciones de favorito del usuario
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * Busca una relación de favorito específica entre dos usuarios.
     *
     * @param userId         identificador del usuario que añadió el favorito
     * @param favoriteUserId identificador del usuario marcado como favorito
     * @return {@link Optional} con la relación si existe, vacío en caso contrario
     */
    Optional<Favorite> findByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);

    /**
     * Comprueba si existe una relación de favorito entre dos usuarios.
     *
     * @param userId         identificador del usuario que añadió el favorito
     * @param favoriteUserId identificador del usuario marcado como favorito
     * @return {@code true} si la relación existe, {@code false} en caso contrario
     */
    boolean existsByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);

    /**
     * Elimina la relación de favorito entre dos usuarios si existe.
     *
     * @param userId         identificador del usuario que tiene el favorito
     * @param favoriteUserId identificador del usuario que se va a eliminar de favoritos
     */
    void deleteByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);
}
