package com.daw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.controller.dto.FavoriteDTO;
import com.daw.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDTO>> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavorites(userId));
    }

    @PostMapping("/{userId}/add/{favoriteUserId}")
    public ResponseEntity<FavoriteDTO> addFavorite(@PathVariable Long userId,
                                                    @PathVariable Long favoriteUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.addFavorite(userId, favoriteUserId));
    }

    @DeleteMapping("/{userId}/remove/{favoriteUserId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId,
                                                @PathVariable Long favoriteUserId) {
        favoriteService.removeFavorite(userId, favoriteUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/check/{favoriteUserId}")
    public ResponseEntity<Map<String, Boolean>> isFavorite(@PathVariable Long userId,
                                                            @PathVariable Long favoriteUserId) {
        return ResponseEntity.ok(Map.of("isFavorite", favoriteService.isFavorite(userId, favoriteUserId)));
    }
}
