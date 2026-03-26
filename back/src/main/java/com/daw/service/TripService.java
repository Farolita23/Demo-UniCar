package com.daw.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return tripMapper.toDto(generalService.findTripById(id));
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
        trip.getRequesters().add(user);
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO cancelRequest(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        trip.getRequesters().removeIf(u -> u.getId().equals(userId));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO acceptPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        User requester = generalService.findUserById(requesterId);
        trip.getRequesters().removeIf(u -> u.getId().equals(requesterId));
        trip.getPassengers().add(requester);
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO rejectPassenger(Long tripId, Long requesterId) {
        Trip trip = generalService.findTripById(tripId);
        trip.getRequesters().removeIf(u -> u.getId().equals(requesterId));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }

    public TripDTO leaveTrip(Long tripId, Long userId) {
        Trip trip = generalService.findTripById(tripId);
        trip.getPassengers().removeIf(u -> u.getId().equals(userId));
        tripRepository.save(trip);
        return tripMapper.toDto(trip);
    }
}
