package com.daw.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final TripCreateMapper tripCreateMapper;
    private final GeneralService generalService;

    public Page<TripDTO> findAll(Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findAll(pageable));
    }

    public TripDTO findById(Long id) {
        Trip trip = generalService.findTripById(id);
        Hibernate.initialize(trip.getPassengers());
        Hibernate.initialize(trip.getRequesters());
        return tripMapper.toDto(trip);
    }

    public Page<TripDTO> findRecommendedTrips(Long idUser, Pageable pageable) {
        User user = generalService.findUserById(idUser);
        Long campusId = user.getUsualCampus().getId();
        Long townId = user.getHomeTown().getId();
        return tripMapper.toPageDto(tripRepository.findRecommendedTrips(campusId, townId, user, pageable));
    }

    public TripDTO create(TripCreateDTO dto) {
        Trip trip = tripCreateMapper.toEntity(dto);
        trip.setCampus(generalService.findCampusById(dto.getIdCampus()));
        trip.setTown(generalService.findTownById(dto.getIdTown()));
        trip.setCar(generalService.findCarById(dto.getIdCar()));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO update(TripCreateDTO dto, Long id) {
        Trip trip = generalService.findTripById(id);
        tripCreateMapper.updateEntityFromDto(dto, trip);
        trip.setCampus(generalService.findCampusById(dto.getIdCampus()));
        trip.setTown(generalService.findTownById(dto.getIdTown()));
        trip.setCar(generalService.findCarById(dto.getIdCar()));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public void delete(Long id) {
        Trip trip = generalService.findTripById(id);
        boolean hasPassengers = !trip.getPassengers().isEmpty();
        if (hasPassengers) {
            throw new HasPassengersException(id);
        }
        tripRepository.delete(trip);
    }

    public Page<TripDTO> getTripsAsADriver(Long idDriver, Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findTripsAsADriver(idDriver, pageable));
    }

    public Page<TripDTO> getTripsAsAPassenger(Long idPassenger, Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findTripsAsAPassenger(idPassenger, pageable));
    }

    public Page<TripDTO> searchTrips(TripSearchDTO filters, Pageable pageable) {
        // Query dinámica: evita el bug de Hibernate con Boolean IS NULL en JPQL
        return tripMapper.toPageDto(
            tripRepository.findAll(TripSpecification.withFilters(filters), pageable)
        );
    }

    public TripDTO requestJoin(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        User user = generalService.findUserById(userId);

        // Validaciones
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

    public TripDTO cancelRequest(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        boolean removed = trip.getRequesters().removeIf(u -> u.getId().equals(userId));
        if (!removed) {
            throw new TripActionException("No tienes una solicitud pendiente en este viaje.");
        }
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO acceptPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        User requester = generalService.findUserById(requesterId);

        // Validar que el solicitante está en requesters
        boolean isRequester = trip.getRequesters().stream()
                .anyMatch(u -> u.getId().equals(requesterId));
        if (!isRequester) {
            throw new TripActionException("Este usuario no tiene una solicitud pendiente.");
        }
        // Validar plazas
        int freeSeats = trip.getCar().getCapacity() - trip.getPassengers().size();
        if (freeSeats <= 0) {
            throw new TripActionException("No hay plazas libres para aceptar más pasajeros.");
        }
        // Validar que no es ya pasajero
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

    public TripDTO rejectPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        boolean removed = trip.getRequesters().removeIf(u -> u.getId().equals(requesterId));
        if (!removed) {
            throw new TripActionException("Este usuario no tiene una solicitud pendiente.");
        }
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

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
     * Viajes sugeridos: basados en campus/towns de viajes solicitados o pasados del usuario.
     * Si no hay datos suficientes, devuelve viajes futuros aleatorios.
     */
    public Page<TripDTO> findSuggestedTrips(Long userId, Pageable pageable) {
        // Recopilar campus y towns de viajes anteriores (como pasajero o solicitante)
        List<Long> campusIds = Stream.concat(
                tripRepository.findCampusIdsByPassenger(userId).stream(),
                tripRepository.findCampusIdsByRequester(userId).stream()
        ).distinct().collect(Collectors.toList());

        List<Long> townIds = Stream.concat(
                tripRepository.findTownIdsByPassenger(userId).stream(),
                tripRepository.findTownIdsByRequester(userId).stream()
        ).distinct().collect(Collectors.toList());

        // También incluir el campus y town habitual del usuario
        User user = generalService.findUserById(userId);
        if (user.getUsualCampus() != null && !campusIds.contains(user.getUsualCampus().getId())) {
            campusIds.add(user.getUsualCampus().getId());
        }
        if (user.getHomeTown() != null && !townIds.contains(user.getHomeTown().getId())) {
            townIds.add(user.getHomeTown().getId());
        }

        if (!campusIds.isEmpty() || !townIds.isEmpty()) {
            // Asegurar listas no vacías para la query
            if (campusIds.isEmpty()) campusIds.add(-1L);
            if (townIds.isEmpty()) townIds.add(-1L);
            Page<TripDTO> suggested = tripMapper.toPageDto(
                    tripRepository.findSuggestedTrips(campusIds, townIds, pageable));
            if (suggested.getTotalElements() > 0) {
                return suggested;
            }
        }

        // Fallback: viajes futuros
        return tripMapper.toPageDto(tripRepository.findFutureTrips(pageable));
    }

    /**
     * Viajes futuros para usuarios no autenticados.
     */
    public Page<TripDTO> findFutureTrips(Pageable pageable) {
        return tripMapper.toPageDto(tripRepository.findFutureTrips(pageable));
    }
}
