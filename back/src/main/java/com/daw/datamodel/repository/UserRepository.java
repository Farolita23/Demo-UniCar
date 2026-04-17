package com.daw.datamodel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    User findByCarsId(Long idCar);

    User findByRatingsReceivedId(Long idRating);

    User findByRatingsDoneId(Long idRating);

    User findByReportsReceivedId(Long idReport);

    User findByReportsDoneId(Long idReport);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchByNameOrUsername(@Param("q") String query);

}
