package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Campus;

/**
 * Repositorio de acceso a datos para la entidad {@link Campus}.
 *
 * Proporciona las operaciones CRUD estándar heredadas de {@link JpaRepository}.
 * Las consultas personalizadas se añadirán en este repositorio cuando los
 * métodos derivados de Spring Data no sean suficientes.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see Campus
 */
@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {

}
