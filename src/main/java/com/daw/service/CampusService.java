package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.controller.dto.CampusDTO;
import com.daw.controller.dto.mapper.CampusCreateMapper;
import com.daw.controller.dto.mapper.CampusMapper;
import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.repository.CampusRepository;
import com.daw.exceptions.EntityWithDependenciesException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los campus universitarios.
 *
 * Proporciona métodos para crear, consultar, actualizar y eliminar campus.
 * La eliminación está bloqueada si el campus tiene viajes o usuarios asociados,
 * garantizando así la integridad referencial del modelo de dominio.
 *
 * @author Jabibi
 * @version 1.0.0
 * @see CampusRepository
 * @see CampusMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CampusService {

	private final CampusRepository campusRepository;
	private final CampusMapper campusMapper;
	private final CampusCreateMapper campusCreateMapper;
	private final GeneralService generalService;

	/**
	 * Recupera todos los campus registrados en el sistema.
	 *
	 * @return lista de {@link CampusDTO} con todos los campus disponibles
	 */
	public List<CampusDTO> findAll() {
		return campusMapper.toListDto(campusRepository.findAll());
	}

	/**
	 * Recupera un campus por su identificador.
	 *
	 * @param id identificador del campus
	 * @return {@link CampusDTO} con los datos del campus encontrado
	 * @throws com.daw.exceptions.CampusNotFoundException si no existe un campus con ese identificador
	 */
	public CampusDTO findById(Long id) {
		return campusMapper.toDto(generalService.findCampusById(id));
	}

	/**
	 * Crea un nuevo campus a partir de los datos proporcionados.
	 *
	 * @param dto datos del campus a crear
	 * @return {@link CampusDTO} con los datos del campus recién creado
	 */
	public CampusDTO create(CampusCreateDTO dto) {
		Campus campus = campusCreateMapper.toEntity(dto);
		Campus createdCampus = campusRepository.save(campus);
		return campusMapper.toDto(createdCampus);
	}

	/**
	 * Actualiza los datos de un campus existente.
	 *
	 * @param dto datos actualizados del campus
	 * @param id  identificador del campus a modificar
	 * @return {@link CampusDTO} con los datos del campus actualizado
	 * @throws com.daw.exceptions.CampusNotFoundException si no existe un campus con ese identificador
	 */
	public CampusDTO update(CampusCreateDTO dto, Long id) {
		Campus campus = generalService.findCampusById(id);
		campusCreateMapper.updateEntityFromDto(dto, campus);
		Campus updatedCampus = campusRepository.save(campus);
		return campusMapper.toDto(updatedCampus);
	}

	/**
	 * Elimina un campus si no tiene viajes ni usuarios asociados.
	 *
	 * @param id identificador del campus a eliminar
	 * @throws com.daw.exceptions.CampusNotFoundException      si no existe un campus con ese identificador
	 * @throws EntityWithDependenciesException si el campus tiene viajes o usuarios vinculados
	 */
	public void delete(Long id) {
		Campus campus = generalService.findCampusById(id);
		boolean hasntTrips = campus.getTrips() == null || campus.getTrips().isEmpty();
		boolean hasntUsers = campus.getUsers() == null || campus.getUsers().isEmpty();
		if(hasntTrips && hasntUsers) {
			campusRepository.delete(campus);
		} else {
			throw new EntityWithDependenciesException("Campus", id);
		}
	}

}
