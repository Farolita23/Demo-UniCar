package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.FavoriteDTO;
import com.daw.controller.dto.mapper.FavoriteMapper;
import com.daw.datamodel.entities.Favorite;
import com.daw.datamodel.repository.FavoriteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las relaciones de favoritos entre usuarios de la plataforma.
 *
 * Permite a los usuarios marcar y desmarcar a otros como favoritos para facilitar
 * la búsqueda de conductores o pasajeros habituales. Aplica validaciones que impiden
 * el auto-marcado y el registro de duplicados.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see FavoriteRepository
 * @see FavoriteMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final GeneralService generalService;

    /**
     * Recupera todos los usuarios marcados como favoritos por el usuario indicado.
     *
     * @param userId identificador del usuario propietario de la lista de favoritos
     * @return lista de {@link FavoriteDTO} con las relaciones de favorito del usuario
     */
    public List<FavoriteDTO> getFavorites(Long userId) {
        return favoriteMapper.toListDto(favoriteRepository.findByUserId(userId));
    }

    /**
     * Añade un usuario a la lista de favoritos de otro usuario.
     *
     * @param userId         identificador del usuario que añade el favorito
     * @param favoriteUserId identificador del usuario que será marcado como favorito
     * @return {@link FavoriteDTO} con los datos de la relación de favorito creada
     * @throws IllegalArgumentException si el usuario intenta añadirse a sí mismo o si ya es favorito
     */
    public FavoriteDTO addFavorite(Long userId, Long favoriteUserId) {
        if (userId.equals(favoriteUserId)) {
            throw new IllegalArgumentException("No puedes añadirte a ti mismo como favorito.");
        }
        if (favoriteRepository.existsByUserIdAndFavoriteUserId(userId, favoriteUserId)) {
            throw new IllegalArgumentException("Este usuario ya está en tus favoritos.");
        }
        Favorite fav = new Favorite();
        fav.setUser(generalService.findUserById(userId));
        fav.setFavoriteUser(generalService.findUserById(favoriteUserId));
        favoriteRepository.save(fav);
        return favoriteMapper.toDto(fav);
    }

    /**
     * Elimina a un usuario de la lista de favoritos de otro usuario.
     *
     * @param userId         identificador del usuario propietario de la lista
     * @param favoriteUserId identificador del usuario a eliminar de favoritos
     */
    public void removeFavorite(Long userId, Long favoriteUserId) {
        favoriteRepository.deleteByUserIdAndFavoriteUserId(userId, favoriteUserId);
    }

    /**
     * Comprueba si un usuario ha marcado a otro como favorito.
     *
     * @param userId         identificador del usuario que potencialmente tiene al otro como favorito
     * @param favoriteUserId identificador del usuario que se comprueba si es favorito
     * @return {@code true} si existe la relación de favorito, {@code false} en caso contrario
     */
    public boolean isFavorite(Long userId, Long favoriteUserId) {
        return favoriteRepository.existsByUserIdAndFavoriteUserId(userId, favoriteUserId);
    }
}
