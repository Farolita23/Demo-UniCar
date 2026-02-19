package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
	
	@Query("SELECT COUNT(t) > 0 FROM Trip t JOIN t.passengers p " + 
	"WHERE (t.driver.id = :idUser1 AND p.id = :idUser2) " + 
	"OR (t.driver.id = :idUser2 AND p.id = :idUser1)")
	boolean existsSharedTrip(@Param("idUser1") Long idUser1, @Param("idUser2") Long idUser2);

}
