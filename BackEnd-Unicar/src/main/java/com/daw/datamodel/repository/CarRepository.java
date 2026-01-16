package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.datamodel.entities.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
