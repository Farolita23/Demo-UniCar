package com.daw.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.datamodel.entities.EntidadBase;

public interface RepositorioBase extends JpaRepository<EntidadBase, Long> {

}
