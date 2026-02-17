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

@Service
@Transactional
@RequiredArgsConstructor
public class CampusService {
	
	private final CampusRepository campusRepository;
	private final CampusMapper campusMapper;
	private final CampusCreateMapper campusCreateMapper;
	private final GeneralService generalService;
	
	public List<CampusDTO> findAll() {
		return campusMapper.toListDto(campusRepository.findAll());
	}
	
	public CampusDTO findById(Long id) {
		return campusMapper.toDto(generalService.findCampusById(id));
	}
	
	public CampusDTO create(CampusCreateDTO dto) {
		Campus campus = campusCreateMapper.toEntity(dto);
		Campus createdCampus = campusRepository.save(campus);
		return campusMapper.toDto(createdCampus);
	}
	
	public CampusDTO update(CampusCreateDTO dto, Long id) {
		Campus campus = generalService.findCampusById(id);
		campusCreateMapper.updateEntityFromDto(dto, campus);
		Campus updatedCampus = campusRepository.save(campus);
		return campusMapper.toDto(updatedCampus);
	}
	
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
