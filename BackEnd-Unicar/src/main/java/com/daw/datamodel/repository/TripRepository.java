package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

}
