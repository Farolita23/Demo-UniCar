package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.TripCreateDTO;
import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.mapper.TripCreateMapper;
import com.daw.controller.dto.mapper.TripMapper;
import com.daw.datamodel.entities.Trip;
import com.daw.datamodel.repository.TripRepository;
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
	
	public List<TripDTO> findAll() {
		return tripMapper.toListDto(tripRepository.findAll());
	}
	
	public TripDTO findById(Long id) {
		return tripMapper.toDto(generalService.findTripById(id));
	}
	
	//Hay que hacer la búsqueda avanzada aún
	
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
		
		if(hasPassengers) {
			throw new HasPassengersException(id);
		}
		
		tripRepository.delete(trip);
	}

}
