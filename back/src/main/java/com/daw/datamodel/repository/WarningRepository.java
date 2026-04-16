package com.daw.datamodel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Warning;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    List<Warning> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);
}
