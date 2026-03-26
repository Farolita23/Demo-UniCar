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

@Service
@Transactional
@RequiredArgsConstructor
public class TownService {
	
	private final TownRepository townRepository;
	private final TownMapper townMapper;
	private final TownCreateMapper townCreateMapper;
	private final GeneralService generalService;
	
	public List<TownDTO> findAll() {
		return townMapper.toListDto(townRepository.findAll());
	}
	
	public TownDTO findById(Long id) {
		return townMapper.toDto(generalService.findTownById(id));
	}
	
	public TownDTO create(TownCreateDTO dto) {
		Town town = townCreateMapper.toEntity(dto);
		townRepository.save(town);
		return townMapper.toDto(town);
	}
	
	public TownDTO update(TownCreateDTO dto, Long id) {
		Town town = generalService.findTownById(id);
		townCreateMapper.updateEntityFromDto(dto, town);
		Town updatedTown = townRepository.save(town);
		return townMapper.toDto(updatedTown);
	}
	
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
