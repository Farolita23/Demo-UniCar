package com.daw.datamodel.repository;

import java.util.List;

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
    /**
     * Recupera todas los campuses ordenados por nombre ascendente.
     *
     * @return lista de campuses ordenadas
     */
    List<Campus> findAllByOrderByNameAsc();
}
