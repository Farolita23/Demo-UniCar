package com.daw.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.controller.dto.RatingDTO;
import com.daw.controller.dto.RatingUpdateDTO;
import com.daw.service.RatingService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<RatingDTO>> findAll() {
        return ResponseEntity.ok(ratingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RatingDTO> create(@RequestBody @Validated RatingCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(dto));
    }

    /**
     * Solo se puede actualizar la puntuación numérica (RatingUpdateDTO),
     * no los usuarios involucrados.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RatingDTO> update(@RequestBody @Validated RatingUpdateDTO dto,
                                             @PathVariable Long id) {
        return ResponseEntity.ok(ratingService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}