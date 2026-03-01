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

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.controller.dto.CampusDTO;
import com.daw.service.CampusService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/campus")
@RequiredArgsConstructor
public class CampusController {

    private final CampusService campusService;

    @GetMapping
    public ResponseEntity<List<CampusDTO>> findAll() {
        return ResponseEntity.ok(campusService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampusDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(campusService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CampusDTO> create(@RequestBody @Validated CampusCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campusService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampusDTO> update(@RequestBody @Validated CampusCreateDTO dto,
                                             @PathVariable Long id) {
        return ResponseEntity.ok(campusService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        campusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}