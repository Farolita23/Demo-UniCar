package com.daw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.controller.dto.CampusDTO;
import com.daw.controller.dto.mapper.CampusCreateMapper;
import com.daw.controller.dto.mapper.CampusMapper;
import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.repository.CampusRepository;
import com.daw.exceptions.CampusNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CampusService {
	
	private final CampusRepository campusRepository;
	private final CampusMapper campusMapper;
	private final CampusCreateMapper campusCreateMapper;
	
	public List<CampusDTO> findAll() {
		return campusMapper.toListDto(campusRepository.findAll());
	}
	
	public CampusDTO findById(Long id) {
		Optional<Campus> campus = campusRepository.findById(id);
		if(campus.isPresent()) {
			return campusMapper.toDto(campus.get());
		} else {
			throw new CampusNotFoundException(id);
		}
	}
	
	public CampusDTO create(CampusCreateDTO dto) {
		Campus campus = campusCreateMapper.toEntity(dto);
		Campus createdCampus = campusRepository.save(campus);
		return campusMapper.toDto(createdCampus);
	}
	
	public CampusDTO update(CampusCreateDTO dto, Long id) {
		Optional<Campus> optCampus = campusRepository.findById(id);
		if(optCampus.isPresent()) {
			Campus campus = campusCreateMapper.toEntity(dto);
			campus.setId(id);
			Campus updatedCampus = campusRepository.save(campus);
			return campusMapper.toDto(updatedCampus);
		} else {
			throw new CampusNotFoundException(id);
		}
	}
	
	public void delete(Long id) {
		Optional<Campus> campus = campusRepository.findById(id);
		if(campus.isPresent()) {
			campusRepository.delete(campus.get());
		} else {
			throw new CampusNotFoundException(id);
		}
	}
	
}
