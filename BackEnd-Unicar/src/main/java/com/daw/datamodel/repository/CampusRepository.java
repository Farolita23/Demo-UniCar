package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Campus;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {

}
