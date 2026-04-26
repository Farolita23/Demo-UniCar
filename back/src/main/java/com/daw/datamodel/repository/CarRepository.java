package com.daw.datamodel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Car;

/**
 * Repositorio de acceso a datos para la entidad {@link Car}.
 *
 * Extiende las operaciones CRUD estándar de {@link JpaRepository} con
 * consultas derivadas específicas del dominio de vehículos.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Car
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Recupera todos los vehículos registrados a nombre de un conductor específico.
     *
     * @param driverId identificador del usuario conductor
     * @return lista de vehículos del conductor; vacía si no tiene ninguno registrado
     */
    List<Car> findByDriverId(Long driverId);

}
