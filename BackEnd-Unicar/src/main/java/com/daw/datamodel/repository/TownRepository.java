package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Town;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

}
