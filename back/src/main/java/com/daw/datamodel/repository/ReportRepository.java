package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
