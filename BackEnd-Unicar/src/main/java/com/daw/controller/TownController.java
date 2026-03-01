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

import com.daw.controller.dto.TownCreateDTO;
import com.daw.controller.dto.TownDTO;
import com.daw.service.TownService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/town")
@RequiredArgsConstructor
public class TownController {

    private final TownService townService;

    @GetMapping
    public ResponseEntity<List<TownDTO>> findAll() {
        return ResponseEntity.ok(townService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TownDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(townService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TownDTO> create(@RequestBody @Validated TownCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(townService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TownDTO> update(@RequestBody @Validated TownCreateDTO dto,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(townService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        townService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
