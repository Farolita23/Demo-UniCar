package com.daw.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.daw.controller.dto.TripCreateDTO;
import com.daw.controller.dto.TripDTO;
import com.daw.service.TripService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    /**
     * GET /api/trip?page=0&size=10&sort=departureDate,asc
     */
    @GetMapping
    public ResponseEntity<Page<TripDTO>> findAll(
            @PageableDefault(size = 10, sort = "departureDate") Pageable pageable) {
        return ResponseEntity.ok(tripService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    /**
     * GET /api/trip/recommended/{idUser}?page=0&size=10
     * Devuelve viajes recomendados para el usuario dado su campus y pueblo habitual.
     */
    @GetMapping("/recommended/{idUser}")
    public ResponseEntity<Page<TripDTO>> findRecommended(
            @PathVariable Long idUser,
            @PageableDefault(size = 10, sort = "departureDate") Pageable pageable) {
        return ResponseEntity.ok(tripService.findRecommendedTrips(idUser, pageable));
    }

    @PostMapping
    public ResponseEntity<TripDTO> create(@RequestBody @Validated TripCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> update(@RequestBody @Validated TripCreateDTO dto,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(tripService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene el historial de viajes donde el usuario ha sido el conductor.
     * GET /api/trip/as-driver/{idDriver}?page=0&size=10
     */
    @GetMapping("/as-driver/{idDriver}")
    public ResponseEntity<Page<TripDTO>> findTripsAsADriver(
            @PathVariable Long idDriver,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tripService.getTripsAsADriver(idDriver, pageable));
    }

    /**
     * Obtiene el historial de viajes donde el usuario ha sido pasajero.
     * GET /api/trip/as-passenger/{idPassenger}?page=0&size=10
     */
    @GetMapping("/as-passenger/{idPassenger}")
    public ResponseEntity<Page<TripDTO>> findTripsAsAPassenger(
            @PathVariable Long idPassenger,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tripService.getTripsAsAPassenger(idPassenger, pageable));
    }
    
}
