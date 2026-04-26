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

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los vehículos.
 *
 * Proporciona métodos para registrar, consultar, actualizar y eliminar vehículos.
 * La eliminación está bloqueada si el vehículo tiene viajes asociados. La asignación
 * del conductor se realiza en el servicio, ya que el mapper ignora la relación {@code driver}.
 *
 * @author Javi Falcons
 * @version 1.0.0
 * @see CarRepository
 * @see CarMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarCreateMapper carCreateMapper;
    private final GeneralService generalService;

    /**
     * Recupera todos los vehículos registrados en el sistema.
     *
     * @return lista de {@link CarDTO} con todos los vehículos disponibles
     */
    public List<CarDTO> findAll() {
        return carMapper.toListDto(carRepository.findAll());
    }

    /**
     * Recupera un vehículo por su identificador.
     *
     * @param id identificador del vehículo
     * @return {@link CarDTO} con los datos del vehículo encontrado
     * @throws CarNotFoundException si no existe un vehículo con ese identificador
     */
    public CarDTO findById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return carMapper.toDto(car);
    }

    /**
     * Recupera todos los vehículos registrados a nombre de un conductor concreto.
     *
     * @param driverId identificador del usuario conductor
     * @return lista de {@link CarDTO} del conductor; vacía si no tiene ningún vehículo registrado
     */
    public List<CarDTO> findByDriverId(Long driverId) {
        return carMapper.toListDto(carRepository.findByDriverId(driverId));
    }

    /**
     * Registra un nuevo vehículo asociándolo al conductor indicado en el DTO.
     *
     * @param dto datos del vehículo a registrar, incluyendo el identificador del conductor
     * @return {@link CarDTO} con los datos del vehículo recién creado
     * @throws com.daw.exceptions.UserNotFoundException si no existe el usuario conductor indicado
     */
    public CarDTO create(CarCreateDTO dto) {
        Car car = carCreateMapper.toEntity(dto);
        car.setDriver(generalService.findUserById(dto.getIdDriver()));
        carRepository.save(car);
        return carMapper.toDto(car);
    }

    /**
     * Actualiza los datos de un vehículo existente, incluyendo la reasignación del conductor.
     *
     * @param dto datos actualizados del vehículo
     * @param id  identificador del vehículo a modificar
     * @return {@link CarDTO} con los datos del vehículo actualizado
     * @throws CarNotFoundException si no existe un vehículo con ese identificador
     */
    public CarDTO update(CarCreateDTO dto, Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        carCreateMapper.updateEntityFromDto(dto, car);
        car.setDriver(generalService.findUserById(dto.getIdDriver()));
        return carMapper.toDto(carRepository.save(car));
    }

    /**
     * Elimina un vehículo si no tiene viajes asociados.
     *
     * @param id identificador del vehículo a eliminar
     * @throws CarNotFoundException              si no existe un vehículo con ese identificador
     * @throws EntityWithDependenciesException si el vehículo tiene viajes vinculados
     */
    public void delete(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        boolean hasNoTrips = car.getTrips() == null || car.getTrips().isEmpty();
        if (!hasNoTrips) throw new EntityWithDependenciesException("Car", id);
        carRepository.delete(car);
    }
}
