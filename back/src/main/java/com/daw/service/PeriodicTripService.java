package com.daw.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.controller.dto.PeriodicTripCreateDTO;
import com.daw.controller.dto.PeriodicTripDTO;
import com.daw.controller.dto.mapper.PeriodicTripCreateMapper;
import com.daw.controller.dto.mapper.PeriodicTripMapper;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.PeriodicTrip;
import com.daw.datamodel.entities.Town;
import com.daw.datamodel.entities.Trip;
import com.daw.datamodel.repository.PeriodicTripRepository;
import com.daw.datamodel.repository.TripRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los viajes periódicos.
 *
 * Proporciona métodos para crear, actualizar, eliminar y consultar viajes periódicos.
 * Al crear un viaje periódico, el servicio genera automáticamente todos los objetos
 * {@link Trip} individuales para el rango de fechas definido.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see PeriodicTripRepository
 * @see PeriodicTripMapper
 * @see TripService
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PeriodicTripService {

    private final PeriodicTripRepository periodicTripRepository;
    private final TripRepository tripRepository;
    private final PeriodicTripMapper periodicTripMapper;
    private final PeriodicTripCreateMapper periodicTripCreateMapper;
    private final GeneralService generalService;

    /**
     * Recupera todos los viajes periódicos de forma paginada.
     *
     * @param pageable configuración de paginación y ordenación
     * @return página de {@link PeriodicTripDTO} con los viajes periódicos disponibles
     */
    public Page<PeriodicTripDTO> findAll(Pageable pageable) {
        return periodicTripRepository.findAll(pageable)
            .map(periodicTripMapper::toDto);
    }

    /**
     * Recupera un viaje periódico por su identificador.
     *
     * @param id identificador del viaje periódico
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico
     * @throws com.daw.exceptions.PeriodicTripNotFoundException si no existe un viaje periódico con ese identificador
     */
    public PeriodicTripDTO findById(Long id) {
        PeriodicTrip periodicTrip = periodicTripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("PeriodicTrip not found with id: " + id));
        return periodicTripMapper.toDto(periodicTrip);
    }

    /**
     * Crea un nuevo viaje periódico y genera automáticamente los viajes individuales.
     *
     * @param dto datos del viaje periódico a crear
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico creado y número de viajes generados
     */
    public PeriodicTripDTO create(PeriodicTripCreateDTO dto) {
        PeriodicTrip periodicTrip = periodicTripCreateMapper.toEntity(dto);
        
        // Cargar relaciones necesarias
        Car car = generalService.findCarById(dto.getIdCar());
        Campus campus = generalService.findCampusById(dto.getIdCampus());
        Town town = generalService.findTownById(dto.getIdTown());
        
        periodicTrip.setCar(car);
        periodicTrip.setCampus(campus);
        periodicTrip.setTown(town);
        
        // Persistir la plantilla
        PeriodicTrip savedPeriodicTrip = periodicTripRepository.save(periodicTrip);
        
        // Generar los viajes individuales
        int tripsGenerated = generateTrips(savedPeriodicTrip);
        
        // Convertir a DTO y añadir información de viajes generados
        PeriodicTripDTO result = periodicTripMapper.toDto(savedPeriodicTrip);
        result.setTripsGenerated(tripsGenerated);
        
        return result;
    }

    /**
     * Genera todos los viajes individuales ({@link Trip}) para una plantilla periódica.
     *
     * Calcula todas las fechas resultantes de combinar {@code startDate}, {@code endDate},
     * {@code daysOfWeek} y {@code repeatIntervalWeeks}, y crea un {@link Trip} para cada una.
     *
     * @param periodicTrip plantilla de viajes periódicos
     * @return número de viajes generados
     */
    private int generateTrips(PeriodicTrip periodicTrip) {
        int count = 0;
        LocalDate currentDate = periodicTrip.getStartDate();
        
        while (!currentDate.isAfter(periodicTrip.getEndDate())) {
            // Comprobar si el día actual está en daysOfWeek
            if (periodicTrip.getDaysOfWeek().contains(currentDate.getDayOfWeek())) {
                // Crear un Trip para esta fecha
                Trip trip = new Trip();
                trip.setCar(periodicTrip.getCar());
                trip.setCampus(periodicTrip.getCampus());
                trip.setTown(periodicTrip.getTown());
                trip.setIsToCampus(periodicTrip.getIsToCampus());
                trip.setDepartureDate(currentDate);
                trip.setDepartureTime(periodicTrip.getDepartureTime());
                trip.setDepartureAddress(periodicTrip.getDepartureAddress());
                trip.setPrice(periodicTrip.getPrice());
                trip.setPeriodicTrip(periodicTrip);
                
                tripRepository.save(trip);
                count++;
            }
            
            // Avanzar a la siguiente semana (según repeatIntervalWeeks)
            currentDate = currentDate.plusWeeks(periodicTrip.getRepeatIntervalWeeks());
        }
        
        return count;
    }

    /**
     * Recupera todos los viajes generados a partir de un viaje periódico.
     *
     * @param periodicTripId identificador del viaje periódico
     * @param pageable configuración de paginación
     * @return página de {@link Trip} generados
     */
    public Page<Trip> findGeneratedTrips(Long periodicTripId, Pageable pageable) {
        return tripRepository.findByPeriodicTripId(periodicTripId, pageable);
    }

    /**
     * Elimina un viaje periódico. Los viajes individuales generados quedarán huérfanos
     * con {@code periodicTrip_id = null} pero no serán eliminados.
     *
     * @param id identificador del viaje periódico a eliminar
     */
    public void delete(Long id) {
        periodicTripRepository.deleteById(id);
    }
}
