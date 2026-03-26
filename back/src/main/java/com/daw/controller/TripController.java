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
import com.daw.controller.dto.TripSearchDTO;
import com.daw.service.TripService;

import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<Page<TripDTO>> findAll(
            @PageableDefault(size = 10, sort = "departureDate") Pageable pageable) {
        return ResponseEntity.ok(tripService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

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
    
    @GetMapping("/as-driver/{idDriver}")
    public ResponseEntity<Page<TripDTO>> findTripsAsADriver(
            @PathVariable Long idDriver,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tripService.getTripsAsADriver(idDriver, pageable));
    }

    @GetMapping("/as-passenger/{idPassenger}")
    public ResponseEntity<Page<TripDTO>> findTripsAsAPassenger(
            @PathVariable Long idPassenger,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(tripService.getTripsAsAPassenger(idPassenger, pageable));
    }
    
    @PostMapping("/search")
    public ResponseEntity<Page<TripDTO>> searchTrips(
            @RequestBody TripSearchDTO filters, Pageable pageable) {
        return ResponseEntity.ok(tripService.searchTrips(filters, pageable));
    }

    /**
     * POST /api/trip/{id}/request/{userId}
     * Pasajero solicita unirse al viaje
     */
    @PostMapping("/{id}/request/{userId}")
    public ResponseEntity<TripDTO> requestJoin(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(tripService.requestJoin(id, userId));
    }

    /**
     * DELETE /api/trip/{id}/request/{userId}
     * Pasajero cancela su solicitud
     */
    @DeleteMapping("/{id}/request/{userId}")
    public ResponseEntity<TripDTO> cancelRequest(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(tripService.cancelRequest(id, userId));
    }

    /**
     * POST /api/trip/{id}/accept/{requesterId}
     * Conductor acepta a un solicitante
     */
    @PostMapping("/{id}/accept/{requesterId}")
    public ResponseEntity<TripDTO> acceptPassenger(
            @PathVariable Long id,
            @PathVariable Long requesterId) {
        return ResponseEntity.ok(tripService.acceptPassenger(id, requesterId));
    }

    /**
     * DELETE /api/trip/{id}/reject/{requesterId}
     * Conductor rechaza a un solicitante
     */
    @DeleteMapping("/{id}/reject/{requesterId}")
    public ResponseEntity<TripDTO> rejectPassenger(
            @PathVariable Long id,
            @PathVariable Long requesterId) {
        return ResponseEntity.ok(tripService.rejectPassenger(id, requesterId));
    }

    /**
     * DELETE /api/trip/{id}/leave/{userId}
     * Pasajero confirmado abandona el viaje
     */
    @DeleteMapping("/{id}/leave/{userId}")
    public ResponseEntity<TripDTO> leaveTrip(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(tripService.leaveTrip(id, userId));
    }
    
}
