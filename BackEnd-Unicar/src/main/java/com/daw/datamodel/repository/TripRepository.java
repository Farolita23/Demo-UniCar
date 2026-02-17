package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
	
	@Query("SELECT COUNT(t) > 0 FROM Trip t JOIN t.passengers p " + 
	"WHERE (t.driver.id = :userId1 AND p.id = :userId2) " + 
	"OR (t.driver.id = :userId2 AND p.id = :userId1)")
	boolean existsSharedTrip(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
