package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Report;

/**
 * Repositorio de acceso a datos para la entidad {@link Report}.
 *
 * Proporciona las operaciones CRUD estándar heredadas de {@link JpaRepository}.
 * La gestión de reportes está restringida al rol de administrador.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @since 2024-09-01
 * @see Report
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
