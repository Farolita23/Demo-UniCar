package com.daw.datamodel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);

    boolean existsByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);

    void deleteByUserIdAndFavoriteUserId(Long userId, Long favoriteUserId);
}
