package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.FavoriteDTO;
import com.daw.controller.dto.mapper.FavoriteMapper;
import com.daw.datamodel.entities.Favorite;
import com.daw.datamodel.repository.FavoriteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final GeneralService generalService;

    public List<FavoriteDTO> getFavorites(Long userId) {
        return favoriteMapper.toListDto(favoriteRepository.findByUserId(userId));
    }

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

    public void removeFavorite(Long userId, Long favoriteUserId) {
        favoriteRepository.deleteByUserIdAndFavoriteUserId(userId, favoriteUserId);
    }

    public boolean isFavorite(Long userId, Long favoriteUserId) {
        return favoriteRepository.existsByUserIdAndFavoriteUserId(userId, favoriteUserId);
    }
}
