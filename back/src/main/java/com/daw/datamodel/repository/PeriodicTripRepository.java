package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.PeriodicTrip;

@Repository
public interface PeriodicTripRepository extends JpaRepository<PeriodicTrip, Long> {
}
