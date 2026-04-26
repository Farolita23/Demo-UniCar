package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.datamodel.entities.Town;

/**
 * Repositorio de acceso a datos para la entidad {@link Town}.
 *
 * Proporciona las operaciones CRUD estándar heredadas de {@link JpaRepository}.
 * La unicidad del código postal se garantiza a nivel de entidad mediante la
 * restricción {@code unique = true} en la columna correspondiente.
 *
 * @author Javuer Falcon
 * @version 1.0.0
 * @see Town
 */
@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

}
