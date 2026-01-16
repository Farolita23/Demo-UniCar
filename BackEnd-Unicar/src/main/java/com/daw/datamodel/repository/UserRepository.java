package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
