package com.daw.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

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
import com.daw.exceptions.PeriodicTripException;
import com.daw.exceptions.PeriodicTripNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los viajes periódicos.
 *
 * Proporciona métodos para crear, consultar, listar y eliminar viajes periódicos.
 * Al crear un viaje periódico, el servicio genera automáticamente todos los objetos
 * {@link Trip} individuales para el rango de fechas definido.
 *
 * @author Javier Falcon
 * @version 1.1.0
 * @see PeriodicTripRepository
 * @see PeriodicTripMapper
 * @see TripService
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PeriodicTripService {

    /** Límite máximo de viajes que puede generar una única plantilla periódica. */
    private static final int MAX_GENERATED_TRIPS = 366;

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
        return periodicTripRepository.findAll(pageable).map(this::toDtoWithCount);
    }

    /**
     * Recupera los viajes periódicos publicados por un conductor concreto.
     *
     * @param driverId identificador del conductor propietario de los vehículos
     * @param pageable configuración de paginación
     * @return página de {@link PeriodicTripDTO} del conductor
     */
    public Page<PeriodicTripDTO> findByDriver(Long driverId, Pageable pageable) {
        return periodicTripRepository.findByDriverId(driverId, pageable).map(this::toDtoWithCount);
    }

    /**
     * Recupera un viaje periódico por su identificador.
     *
     * @param id identificador del viaje periódico
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico
     * @throws PeriodicTripNotFoundException si no existe un viaje periódico con ese identificador
     */
    public PeriodicTripDTO findById(Long id) {
        return toDtoWithCount(findEntityById(id));
    }

    /**
     * Crea un nuevo viaje periódico y genera automáticamente los viajes individuales.
     *
     * @param dto datos del viaje periódico a crear
     * @return {@link PeriodicTripDTO} con los datos del viaje periódico creado y número de viajes generados
     * @throws PeriodicTripException si el rango de fechas no es válido o no genera ningún viaje
     */
    public PeriodicTripDTO create(PeriodicTripCreateDTO dto) {
        validate(dto);

        PeriodicTrip periodicTrip = periodicTripCreateMapper.toEntity(dto);

        Car car = generalService.findCarById(dto.getIdCar());
        Campus campus = generalService.findCampusById(dto.getIdCampus());
        Town town = generalService.findTownById(dto.getIdTown());

        periodicTrip.setCar(car);
        periodicTrip.setCampus(campus);
        periodicTrip.setTown(town);

        PeriodicTrip savedPeriodicTrip = periodicTripRepository.save(periodicTrip);

        int tripsGenerated = generateTrips(savedPeriodicTrip);
        if (tripsGenerated == 0) {
            // Fuerza el rollback de la plantilla ya persistida
            throw new PeriodicTripException(
                "La combinación de días de la semana y rango de fechas no genera ningún viaje.");
        }

        PeriodicTripDTO result = periodicTripMapper.toDto(savedPeriodicTrip);
        result.setTripsGenerated(tripsGenerated);
        return result;
    }

    /**
     * Valida las reglas de negocio de la definición periódica antes de persistirla.
     *
     * @param dto datos del viaje periódico a validar
     * @throws PeriodicTripException si alguna regla no se cumple
     */
    private void validate(PeriodicTripCreateDTO dto) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new PeriodicTripException("Las fechas de inicio y fin son obligatorias.");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new PeriodicTripException("La fecha de fin no puede ser anterior a la de inicio.");
        }
        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new PeriodicTripException("La fecha de inicio no puede ser anterior a hoy.");
        }
        if (dto.getEndDate().isAfter(dto.getStartDate().plusYears(1))) {
            throw new PeriodicTripException("El rango de fechas no puede superar un año.");
        }
        if (dto.getDaysOfWeek() == null || dto.getDaysOfWeek().isEmpty()) {
            throw new PeriodicTripException("Debes seleccionar al menos un día de la semana.");
        }
        if (dto.getRepeatIntervalWeeks() == null || dto.getRepeatIntervalWeeks() < 1) {
            throw new PeriodicTripException("El intervalo de repetición debe ser de al menos una semana.");
        }
    }

    /**
     * Genera todos los viajes individuales ({@link Trip}) para una plantilla periódica.
     *
     * Recorre el rango {@code [startDate, endDate]} día a día y crea un {@link Trip}
     * por cada fecha cuyo día de la semana esté en {@code daysOfWeek} y cuya semana
     * sea múltiplo de {@code repeatIntervalWeeks} contando desde la semana de inicio
     * (las semanas se anclan al lunes para que el conteo sea estable sea cual sea el
     * día en que caiga {@code startDate}).
     *
     * @param periodicTrip plantilla de viajes periódicos
     * @return número de viajes generados
     */
    private int generateTrips(PeriodicTrip periodicTrip) {
        final LocalDate start = periodicTrip.getStartDate();
        final LocalDate end = periodicTrip.getEndDate();
        final int interval = Math.max(1, periodicTrip.getRepeatIntervalWeeks());
        final LocalDate startWeekMonday = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        int count = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (!periodicTrip.getDaysOfWeek().contains(date.getDayOfWeek())) {
                continue;
            }
            LocalDate weekMonday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            long weeksFromStart = ChronoUnit.WEEKS.between(startWeekMonday, weekMonday);
            if (weeksFromStart % interval != 0) {
                continue;
            }

            Trip trip = new Trip();
            trip.setCar(periodicTrip.getCar());
            trip.setCampus(periodicTrip.getCampus());
            trip.setTown(periodicTrip.getTown());
            trip.setIsToCampus(periodicTrip.getIsToCampus());
            trip.setDepartureDate(date);
            trip.setDepartureTime(periodicTrip.getDepartureTime());
            trip.setDepartureAddress(periodicTrip.getDepartureAddress());
            trip.setPrice(periodicTrip.getPrice());
            trip.setPeriodicTrip(periodicTrip);

            tripRepository.save(trip);
            count++;

            if (count > MAX_GENERATED_TRIPS) {
                throw new PeriodicTripException(
                    "La definición genera demasiados viajes (máximo " + MAX_GENERATED_TRIPS + ").");
            }
        }
        return count;
    }

    /**
     * Recupera todos los viajes generados a partir de un viaje periódico.
     *
     * @param periodicTripId identificador del viaje periódico
     * @param pageable configuración de paginación
     * @return página de {@link Trip} generados
     * @throws PeriodicTripNotFoundException si no existe un viaje periódico con ese identificador
     */
    public Page<Trip> findGeneratedTrips(Long periodicTripId, Pageable pageable) {
        findEntityById(periodicTripId); // valida existencia -> 404 coherente
        return tripRepository.findByPeriodicTripId(periodicTripId, pageable);
    }

    /**
     * Elimina un viaje periódico. Los viajes individuales generados quedarán huérfanos
     * con {@code periodic_trip_id = null} (por la FK {@code ON DELETE SET NULL}) pero
     * no serán eliminados.
     *
     * @param id identificador del viaje periódico a eliminar
     * @throws PeriodicTripNotFoundException si no existe un viaje periódico con ese identificador
     */
    public void delete(Long id) {
        PeriodicTrip periodicTrip = findEntityById(id);
        periodicTripRepository.delete(periodicTrip);
    }

    /**
     * Recupera la entidad {@link PeriodicTrip} o lanza {@link PeriodicTripNotFoundException}.
     *
     * @param id identificador del viaje periódico
     * @return entidad encontrada
     */
    private PeriodicTrip findEntityById(Long id) {
        return periodicTripRepository.findById(id)
            .orElseThrow(() -> new PeriodicTripNotFoundException(id));
    }

    /**
     * Convierte la entidad a DTO rellenando el número real de viajes generados.
     *
     * @param periodicTrip entidad a convertir
     * @return DTO con {@code tripsGenerated} poblado
     */
    private PeriodicTripDTO toDtoWithCount(PeriodicTrip periodicTrip) {
        PeriodicTripDTO dto = periodicTripMapper.toDto(periodicTrip);
        dto.setTripsGenerated((int) tripRepository.countByPeriodicTripId(periodicTrip.getId()));
        return dto;
    }
}
