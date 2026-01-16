package com.daw.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.controller.dto.mapper.MapperBase;
import com.daw.datamodel.repository.RepositorioBase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicioBase {

    private final RepositorioBase repositorioBase;
    private final MapperBase mapperBase;
}
