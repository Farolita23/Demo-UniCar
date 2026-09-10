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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.controller.dto.PeriodicTripCreateDTO;
import com.daw.controller.dto.PeriodicTripDTO;
import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.mapper.TripMapper;
import com.daw.datamodel.entities.Trip;
import com.daw.service.PeriodicTripService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de viajes periódicos.
 *
 * Proporciona endpoints para crear, consultar, listar y eliminar viajes periódicos,
 * así como para recuperar los viajes individuales generados a partir de una plantilla.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see PeriodicTripService
 */
@RestController
@Validated
@RequestMapping("/api/periodic-trip")
@RequiredArgsConstructor
public class PeriodicTripController {

    private final PeriodicTripService periodicTripService;
    private final TripMapper tripMapper;

    /**
     * Recupera todos los viajes periódicos de forma paginada.
     *
     * @param pageable configuración de paginación y ordenación
     * @return página de {@link PeriodicTripDTO}
     */
    @GetMapping
    public ResponseEntity<Page<PeriodicTripDTO>> findAll(
            @PageableDefault(size = 10, sort = "startDate") Pageable pageable) {
        return ResponseEntity.ok(periodicTripService.findAll(pageable));
    }

    /**
     * Recupera los viajes periódicos publicados por un conductor concreto.
     *
     * @param driverId identificador del conductor
     * @param pageable configuración de paginación
     * @return página de {@link PeriodicTripDTO} del conductor
     */
    @GetMapping("/as-driver/{driverId}")
    public ResponseEntity<Page<PeriodicTripDTO>> findByDriver(
            @PathVariable Long driverId,
            @PageableDefault(size = 20, sort = "startDate") Pageable pageable) {
        return ResponseEntity.ok(periodicTripService.findByDriver(driverId, pageable));
    }

    /**
     * Recupera un viaje periódico por su identificador.
     *
     * @param id identificador del viaje periódico
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico
     */
    @GetMapping("/{id}")
    public ResponseEntity<PeriodicTripDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(periodicTripService.findById(id));
    }

    /**
     * Crea un nuevo viaje periódico y genera automáticamente los viajes individuales.
     *
     * @param dto datos del viaje periódico a crear
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico creado
     */
    @PostMapping
    public ResponseEntity<PeriodicTripDTO> create(@RequestBody @Validated PeriodicTripCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(periodicTripService.create(dto));
    }

    /**
     * Elimina un viaje periódico. Los viajes individuales generados quedarán huérfanos.
     *
     * @param id identificador del viaje periódico a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        periodicTripService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recupera todos los viajes generados a partir de un viaje periódico.
     *
     * @param periodicTripId identificador del viaje periódico
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} generados
     */
    @GetMapping("/{periodicTripId}/trips")
    public ResponseEntity<Page<TripDTO>> findGeneratedTrips(
            @PathVariable Long periodicTripId,
            @PageableDefault(size = 20, sort = "departureDate") Pageable pageable) {
        Page<Trip> generatedTrips = periodicTripService.findGeneratedTrips(periodicTripId, pageable);
        Page<TripDTO> tripDtos = generatedTrips.map(tripMapper::toDto);
        return ResponseEntity.ok(tripDtos);
    }
}
