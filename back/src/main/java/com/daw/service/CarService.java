package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.controller.dto.CarDTO;
import com.daw.controller.dto.mapper.CarCreateMapper;
import com.daw.controller.dto.mapper.CarMapper;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.repository.CarRepository;
import com.daw.exceptions.CarNotFoundException;
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
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return carMapper.toDto(car);
    }

    /** Devuelve todos los coches de un conductor. Endpoint: GET /api/car/user/{driverId} */
    public List<CarDTO> findByDriverId(Long driverId) {
        return carMapper.toListDto(carRepository.findByDriverId(driverId));
    }

    public CarDTO create(CarCreateDTO dto) {
        Car car = carCreateMapper.toEntity(dto);
        car.setDriver(generalService.findUserById(dto.getIdDriver()));
        carRepository.save(car);
        return carMapper.toDto(car);
    }

    public CarDTO update(CarCreateDTO dto, Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        carCreateMapper.updateEntityFromDto(dto, car);
        car.setDriver(generalService.findUserById(dto.getIdDriver()));
        return carMapper.toDto(carRepository.save(car));
    }

    public void delete(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        boolean hasNoTrips = car.getTrips() == null || car.getTrips().isEmpty();
        if (!hasNoTrips) throw new EntityWithDependenciesException("Car", id);
        carRepository.delete(car);
    }
}
