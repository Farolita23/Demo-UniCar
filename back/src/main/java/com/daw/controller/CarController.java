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

import com.daw.controller.dto.CarCreateDTO;
import com.daw.controller.dto.CarDTO;
import com.daw.service.CarService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> findAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    /** GET /api/car/user/{driverId} — coches del conductor */
    @GetMapping("/user/{driverId}")
    public ResponseEntity<List<CarDTO>> findByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(carService.findByDriverId(driverId));
    }

    @PostMapping
    public ResponseEntity<CarDTO> create(@RequestBody @Validated CarCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> update(@RequestBody @Validated CarCreateDTO dto,
                                         @PathVariable Long id) {
        return ResponseEntity.ok(carService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
