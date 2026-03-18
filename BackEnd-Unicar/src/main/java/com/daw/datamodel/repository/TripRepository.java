package com.daw.datamodel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Trip;
import com.daw.datamodel.entities.User;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
	
	@Query("SELECT COUNT(t) > 0 FROM Trip t JOIN t.passengers p " + 
		   "WHERE (t.car.driver.id = :idUser1 AND p.id = :idUser2) " + 
		   "OR (t.car.driver.id = :idUser2 AND p.id = :idUser1)")
	boolean existsSharedTrip(@Param("idUser1") Long idUser1, @Param("idUser2") Long idUser2);
	
	@Query("SELECT t FROM Trip t WHERE " +
		       "t.campus.id = :campusId AND t.town.id = :townId " +
		       "AND t.departureDate >= CURRENT_DATE " +
		       "ORDER BY " +
		       "(SELECT COUNT(pt) FROM Trip pt " +
		       " WHERE :user MEMBER OF pt.passengers " +
		       " AND pt.car.driver = t.car.driver) DESC, " + 
		       "t.departureDate ASC, t.departureTime ASC")
	Page<Trip> findRecommendedTrips(@Param("campusId") Long campusId, 
		    						@Param("townId") Long townId, 
		    						@Param("user") User user, 
		    						Pageable pageable);
	
	@Query("SELECT t FROM Trip t WHERE t.car.driver.id = :idDriver AND t.departureDate < CURRENT_DATE " + 
		   "ORDER BY t.departureDate DESC, t.departureTime DESC")
	Page<Trip> findTripsAsADriver(@Param("idDriver") Long idDriver, Pageable pageable);
	
	@Query("SELECT t FROM Trip t JOIN t.passengers p WHERE p.id = :idPassenger AND t.departureDate < CURRENT_DATE " + 
			   "ORDER BY t.departureDate DESC, t.departureTime DESC")
	Page<Trip> findTripsAsAPassenger(@Param("idPassenger") Long idPassenger, Pageable pageable);

}
