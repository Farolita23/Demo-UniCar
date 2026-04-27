package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.TownCreateDTO;
import com.daw.controller.dto.TownDTO;
import com.daw.controller.dto.mapper.TownCreateMapper;
import com.daw.controller.dto.mapper.TownMapper;
import com.daw.datamodel.entities.Town;
import com.daw.datamodel.repository.TownRepository;
import com.daw.exceptions.EntityWithDependenciesException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con las localidades.
 *
 * Proporciona métodos para crear, consultar, actualizar y eliminar localidades.
 * La eliminación está bloqueada si la localidad tiene viajes o usuarios residentes
 * asociados, garantizando la integridad referencial del modelo de dominio.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @since 2024-09-01
 * @see TownRepository
 * @see TownMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TownService {

	private final TownRepository townRepository;
	private final TownMapper townMapper;
	private final TownCreateMapper townCreateMapper;
	private final GeneralService generalService;

	/**
	 * Recupera todas las localidades registradas en el sistema.
	 *
	 * @return lista de {@link TownDTO} con todas las localidades disponibles
	 */
	public List<TownDTO> findAll() {
		return townMapper.toListDto(townRepository.findAll());
	}

	/**
	 * Recupera una localidad por su identificador.
	 *
	 * @param id identificador de la localidad
	 * @return {@link TownDTO} con los datos de la localidad encontrada
	 * @throws com.daw.exceptions.TownNotFoundException si no existe una localidad con ese identificador
	 */
	public TownDTO findById(Long id) {
		return townMapper.toDto(generalService.findTownById(id));
	}

	/**
	 * Crea una nueva localidad a partir de los datos proporcionados.
	 *
	 * @param dto datos de la localidad a crear
	 * @return {@link TownDTO} con los datos de la localidad recién creada
	 */
	public TownDTO create(TownCreateDTO dto) {
		Town town = townCreateMapper.toEntity(dto);
		townRepository.save(town);
		return townMapper.toDto(town);
	}

	/**
	 * Actualiza los datos de una localidad existente.
	 *
	 * @param dto datos actualizados de la localidad
	 * @param id  identificador de la localidad a modificar
	 * @return {@link TownDTO} con los datos de la localidad actualizada
	 * @throws com.daw.exceptions.TownNotFoundException si no existe una localidad con ese identificador
	 */
	public TownDTO update(TownCreateDTO dto, Long id) {
		Town town = generalService.findTownById(id);
		townCreateMapper.updateEntityFromDto(dto, town);
		Town updatedTown = townRepository.save(town);
		return townMapper.toDto(updatedTown);
	}

	/**
	 * Elimina una localidad si no tiene viajes ni usuarios residentes asociados.
	 *
	 * @param id identificador de la localidad a eliminar
	 * @throws com.daw.exceptions.TownNotFoundException si no existe una localidad con ese identificador
	 * @throws EntityWithDependenciesException          si la localidad tiene viajes o usuarios vinculados
	 */
	public void delete(Long id) {
		Town town = generalService.findTownById(id);
		boolean hasntTrips = town.getTrips() == null || town.getTrips().isEmpty();
		boolean hasntUsers = town.getUsers() == null || town.getUsers().isEmpty();
		if(hasntTrips && hasntUsers) {
			townRepository.delete(town);
		} else {
			throw new EntityWithDependenciesException("Town", id);
		}
	}

}
