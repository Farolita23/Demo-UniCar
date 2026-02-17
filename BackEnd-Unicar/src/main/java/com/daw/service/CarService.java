package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.controller.dto.CarDTO;
import com.daw.controller.dto.mapper.CarCreateMapper;
import com.daw.controller.dto.mapper.CarMapper;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.repository.CarRepository;
import com.daw.exceptions.EntityWithDependenciesException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {
	
	private final CarRepository carRepository;
	private final CarMapper carMapper;
	private final CarCreateMapper carCreateMapper;
	private final GeneralService generalService;
	
	public List<CarDTO> findAll() {
		return carMapper.toListDto(carRepository.findAll());
	}
	
	public CarDTO findById(Long id) {
		return carMapper.toDto(generalService.findCarById(id));
	}
	
	public CarDTO create(CarCreateDTO dto) {
		Car car = carCreateMapper.toEntity(dto);
		car.setDriver(generalService.findUserById(dto.getIdDriver()));
		carRepository.save(car);
		return carMapper.toDto(car);
	}
	
	public CarDTO update(CarCreateDTO dto, Long id) {
		Car car = generalService.findCarById(id);
		carCreateMapper.updateEntityFromDto(dto, car);
		car.setDriver(generalService.findUserById(dto.getIdDriver()));
		Car updatedCar = carRepository.save(car);
		return carMapper.toDto(updatedCar);
	}
	
	public void delete(Long id) {
		Car car = generalService.findCarById(id);
		boolean hasntTrips = car.getTrips() == null || car.getTrips().isEmpty();
		if(hasntTrips) {
			carRepository.delete(car);
		} else {
			throw new EntityWithDependenciesException("Car", id);
		}
	}

}
