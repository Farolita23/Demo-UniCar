package com.daw.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.daw.controller.dto.TripCreateDTO;
import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.TripSearchDTO;
import com.daw.controller.dto.mapper.TripCreateMapper;
import com.daw.controller.dto.mapper.TripMapper;
import com.daw.datamodel.entities.Trip;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.TripRepository;
import com.daw.datamodel.repository.TripSpecification;
import com.daw.exceptions.HasPassengersException;
import com.daw.exceptions.TripActionException;

import org.hibernate.Hibernate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los viajes compartidos.
 *
 * Proporciona métodos para crear, actualizar, eliminar y consultar viajes, así como
 * para gestionar el ciclo de vida de los pasajeros: solicitud, aceptación, rechazo y abandono.
 * Implementa la lógica de recomendaciones personalizadas y viajes sugeridos basados en
 * el historial de viajes del usuario.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see TripRepository
 * @see TripMapper
 * @see TripSpecification
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final TripCreateMapper tripCreateMapper;
    private final GeneralService generalService;

    /**
     * Recupera todos los viajes de forma paginada.
     *
     * @param pageable configuración de paginación y ordenación
     * @return página de {@link TripDTO} con los viajes disponibles
     */
    public Page<TripDTO> findAll(Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findAll(pageable));
    }

    /**
     * Recupera un viaje por su identificador, inicializando explícitamente las colecciones
     * de pasajeros y solicitantes para evitar {@code LazyInitializationException}.
     *
     * @param id identificador del viaje
     * @return {@link TripDTO} con los datos completos del viaje
     * @throws com.daw.exceptions.TripNotFoundException si no existe un viaje con ese identificador
     */
    public TripDTO findById(Long id) {
        Trip trip = generalService.findTripById(id);
        Hibernate.initialize(trip.getPassengers());
        Hibernate.initialize(trip.getRequesters());
        return tripMapper.toDto(trip);
    }

    /**
     * Recupera viajes recomendados para el usuario basándose en su campus y localidad habituales,
     * ordenados por afinidad con conductores previos y fecha de salida.
     *
     * @param idUser   identificador del usuario para el que se calculan recomendaciones
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} con los viajes recomendados
     */
    public Page<TripDTO> findRecommendedTrips(Long idUser, Pageable pageable) {
        User user = generalService.findUserById(idUser);
        Long campusId = user.getUsualCampus().getId();
        Long townId = user.getHomeTown().getId();
        return tripMapper.toPageDto(tripRepository.findRecommendedTrips(campusId, townId, user, pageable));
    }

    /**
     * Crea un nuevo viaje asociando el campus, la localidad y el vehículo indicados en el DTO.
     *
     * @param dto datos del viaje a crear
     * @return {@link TripDTO} con los datos del viaje recién creado
     */
    public TripDTO create(TripCreateDTO dto) {
        Trip trip = tripCreateMapper.toEntity(dto);
        trip.setCampus(generalService.findCampusById(dto.getIdCampus()));
        trip.setTown(generalService.findTownById(dto.getIdTown()));
        trip.setCar(generalService.findCarById(dto.getIdCar()));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Actualiza los datos de un viaje existente, reasignando campus, localidad y vehículo.
     *
     * @param dto datos actualizados del viaje
     * @param id  identificador del viaje a modificar
     * @return {@link TripDTO} con los datos del viaje actualizado
     * @throws com.daw.exceptions.TripNotFoundException si no existe un viaje con ese identificador
     */
    public TripDTO update(TripCreateDTO dto, Long id) {
        Trip trip = generalService.findTripById(id);
        tripCreateMapper.updateEntityFromDto(dto, trip);
        trip.setCampus(generalService.findCampusById(dto.getIdCampus()));
        trip.setTown(generalService.findTownById(dto.getIdTown()));
        trip.setCar(generalService.findCarById(dto.getIdCar()));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Elimina un viaje si no tiene pasajeros confirmados.
     *
     * @param id identificador del viaje a eliminar
     * @throws com.daw.exceptions.TripNotFoundException si no existe un viaje con ese identificador
     * @throws HasPassengersException                   si el viaje tiene pasajeros confirmados
     */
    public void delete(Long id) {
        Trip trip = generalService.findTripById(id);
        boolean hasPassengers = !trip.getPassengers().isEmpty();
        if (hasPassengers) {
            throw new HasPassengersException(id);
        }
        tripRepository.delete(trip);
    }

    /**
     * Recupera los viajes en los que el usuario actúa como conductor, de forma paginada.
     *
     * @param idDriver identificador del conductor
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} de viajes como conductor
     */
    public Page<TripDTO> getTripsAsADriver(Long idDriver, Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findTripsAsADriver(idDriver, pageable));
    }

    /**
     * Recupera los viajes en los que el usuario participa como pasajero confirmado, de forma paginada.
     *
     * @param idPassenger identificador del pasajero
     * @param pageable    configuración de paginación
     * @return página de {@link TripDTO} de viajes como pasajero
     */
    public Page<TripDTO> getTripsAsAPassenger(Long idPassenger, Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findTripsAsAPassenger(idPassenger, pageable));
    }

    /**
     * Busca viajes futuros aplicando los filtros opcionales del DTO mediante consulta dinámica.
     *
     * <p>Usa {@link TripSpecification} con Criteria API para evitar el bug de Hibernate
     * con operaciones {@code Boolean IS NULL} en JPQL estático.</p>
     *
     * @param filters criterios de búsqueda opcionales
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} con los resultados filtrados
     */
    public Page<TripDTO> searchTrips(TripSearchDTO filters, Pageable pageable) {
        return tripMapper.toPageDto(
            tripRepository.findAll(TripSpecification.withFilters(filters), pageable)
        );
    }

    /**
     * Registra la solicitud de un usuario para unirse a un viaje como pasajero.
     *
     * @param tripId identificador del viaje
     * @param userId identificador del usuario solicitante
     * @return {@link TripDTO} con el estado actualizado del viaje
     * @throws TripActionException si el usuario es el conductor, ya ha solicitado plaza,
     *                             ya es pasajero o no hay plazas libres
     */
    public TripDTO requestJoin(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        User user = generalService.findUserById(userId);

        boolean isDriver = trip.getCar().getDriver().getId().equals(userId);
        if (isDriver) {
            throw new TripActionException("No puedes solicitar plaza en tu propio viaje.");
        }
        boolean alreadyRequested = trip.getRequesters().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyRequested) {
            throw new TripActionException("Ya has solicitado plaza en este viaje.");
        }
        boolean alreadyPassenger = trip.getPassengers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyPassenger) {
            throw new TripActionException("Ya eres pasajero de este viaje.");
        }
        int freeSeats = trip.getCar().getCapacity() - trip.getPassengers().size();
        if (freeSeats <= 0) {
            throw new TripActionException("No hay plazas libres en este viaje.");
        }

        trip.getRequesters().add(user);
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Cancela la solicitud de un usuario para unirse a un viaje.
     *
     * @param tripId identificador del viaje
     * @param userId identificador del usuario que cancela su solicitud
     * @return {@link TripDTO} con el estado actualizado del viaje
     * @throws TripActionException si el usuario no tiene una solicitud pendiente en el viaje
     */
    public TripDTO cancelRequest(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        boolean removed = trip.getRequesters().removeIf(u -> u.getId().equals(userId));
        if (!removed) {
            throw new TripActionException("No tienes una solicitud pendiente en este viaje.");
        }
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Acepta a un solicitante como pasajero confirmado del viaje.
     *
     * @param tripId     identificador del viaje
     * @param requesterId identificador del usuario solicitante a aceptar
     * @return {@link TripDTO} con el estado actualizado del viaje
     * @throws TripActionException si el usuario no tiene solicitud pendiente, no hay plazas o ya es pasajero
     */
    public TripDTO acceptPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        User requester = generalService.findUserById(requesterId);

        boolean isRequester = trip.getRequesters().stream()
                .anyMatch(u -> u.getId().equals(requesterId));
        if (!isRequester) {
            throw new TripActionException("Este usuario no tiene una solicitud pendiente.");
        }
        int freeSeats = trip.getCar().getCapacity() - trip.getPassengers().size();
        if (freeSeats <= 0) {
            throw new TripActionException("No hay plazas libres para aceptar más pasajeros.");
        }
        boolean alreadyPassenger = trip.getPassengers().stream()
                .anyMatch(u -> u.getId().equals(requesterId));
        if (alreadyPassenger) {
            throw new TripActionException("Este usuario ya es pasajero del viaje.");
        }

        trip.getRequesters().removeIf(u -> u.getId().equals(requesterId));
        trip.getPassengers().add(requester);
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Rechaza la solicitud de un usuario para unirse al viaje.
     *
     * @param tripId     identificador del viaje
     * @param requesterId identificador del usuario solicitante a rechazar
     * @return {@link TripDTO} con el estado actualizado del viaje
     * @throws TripActionException si el usuario no tiene una solicitud pendiente
     */
    public TripDTO rejectPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        boolean removed = trip.getRequesters().removeIf(u -> u.getId().equals(requesterId));
        if (!removed) {
            throw new TripActionException("Este usuario no tiene una solicitud pendiente.");
        }
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Permite a un pasajero confirmado abandonar voluntariamente el viaje.
     *
     * @param tripId identificador del viaje
     * @param userId identificador del pasajero que abandona
     * @return {@link TripDTO} con el estado actualizado del viaje
     * @throws TripActionException si el usuario no es pasajero del viaje
     */
    public TripDTO leaveTrip(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        boolean removed = trip.getPassengers().removeIf(u -> u.getId().equals(userId));
        if (!removed) {
            throw new TripActionException("No eres pasajero de este viaje.");
        }
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    /**
     * Recupera viajes sugeridos para el usuario basándose en su historial de campus y localidades.
     *
     * <p>Combina los campus y localidades de viajes anteriores (como pasajero o solicitante)
     * con el campus y localidad habitual del usuario. Si no hay suficiente historial,
     * devuelve viajes futuros como alternativa.</p>
     *
     * @param userId   identificador del usuario para el que se calculan las sugerencias
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} con los viajes sugeridos o futuros como fallback
     */
    public Page<TripDTO> findSuggestedTrips(Long userId, Pageable pageable) {
        List<Long> campusIds = Stream.concat(
                tripRepository.findCampusIdsByPassenger(userId).stream(),
                tripRepository.findCampusIdsByRequester(userId).stream()
        ).distinct().collect(Collectors.toList());

        List<Long> townIds = Stream.concat(
                tripRepository.findTownIdsByPassenger(userId).stream(),
                tripRepository.findTownIdsByRequester(userId).stream()
        ).distinct().collect(Collectors.toList());

        User user = generalService.findUserById(userId);
        if (user.getUsualCampus() != null && !campusIds.contains(user.getUsualCampus().getId())) {
            campusIds.add(user.getUsualCampus().getId());
        }
        if (user.getHomeTown() != null && !townIds.contains(user.getHomeTown().getId())) {
            townIds.add(user.getHomeTown().getId());
        }

        if (!campusIds.isEmpty() || !townIds.isEmpty()) {
            // Asegurar listas no vacías para evitar que la query falle con colecciones vacías
            if (campusIds.isEmpty()) campusIds.add(-1L);
            if (townIds.isEmpty()) townIds.add(-1L);
            Page<TripDTO> suggested = tripMapper.toPageDto(
                    tripRepository.findSuggestedTrips(campusIds, townIds, pageable));
            if (suggested.getTotalElements() > 0) {
                return suggested;
            }
        }

        // Fallback: viajes futuros cuando no hay historial suficiente
        return tripMapper.toPageDto(tripRepository.findFutureTrips(pageable));
    }

    /**
     * Recupera los viajes con fecha de salida igual o posterior a la fecha actual.
     *
     * <p>Endpoint pensado para usuarios no autenticados que deseen explorar la plataforma.</p>
     *
     * @param pageable configuración de paginación
     * @return página de {@link TripDTO} con viajes futuros disponibles
     */
    public Page<TripDTO> findFutureTrips(Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findFutureTrips(pageable));
    }
}
