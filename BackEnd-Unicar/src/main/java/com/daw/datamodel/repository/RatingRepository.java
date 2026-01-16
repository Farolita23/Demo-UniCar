package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

}
