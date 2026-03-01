package com.daw.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.mapper.UserCreateMapper;
import com.daw.controller.dto.mapper.UserMapper;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.DuplicateEmailException;
import com.daw.exceptions.DuplicateUsernameException;
import com.daw.exceptions.EntityWithDependenciesException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserCreateMapper userCreateMapper;
	private final GeneralService generalService;
	private final PasswordEncoder passwordEncoder;
	
	public List<UserDTO> findAll() {
		return userMapper.toListDto(userRepository.findAll());
	}
	
	public UserDTO findById(Long id) {
		return userMapper.toDto(generalService.findUserById(id));
	}
	
	public UserDTO create(UserCreateDTO dto) {
		String username = dto.getUsername();
		String email = dto.getEmail();
		
		if(generalService.existsByUsername(username)) {
			throw new DuplicateUsernameException();
		}
		
		if(generalService.existsByEmail(email)) {
			throw new DuplicateEmailException();
		}
		
		User user = userCreateMapper.toEntity(dto);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setUsualCampus(generalService.findCampusById(dto.getIdUsualCampus()));
		user.setHomeTown(generalService.findTownById(dto.getIdHomeTown()));
		userRepository.save(user);
		
		return userMapper.toDto(user);
	}
	
	public UserDTO update(UserCreateDTO dto, Long id) {
		User user = generalService.findUserById(id);
		
		if(!user.getUsername().equals(dto.getUsername()) && generalService.existsByUsername(dto.getUsername())) {
			throw new DuplicateUsernameException();
		}
		
		if(!user.getEmail().equals(dto.getEmail()) && generalService.existsByEmail(dto.getEmail())) {
			throw new DuplicateEmailException();
		}
		
		userCreateMapper.updateEntityFromDto(dto, user);
		
		if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		user.setUsualCampus(generalService.findCampusById(dto.getIdUsualCampus()));
		user.setHomeTown(generalService.findTownById(dto.getIdHomeTown()));
		
		User updatedUser = userRepository.save(user);
		return userMapper.toDto(updatedUser);
	}
	
	public void delete(Long id) {
		User user = generalService.findUserById(id);
		
		boolean hasntCars = user.getCars().isEmpty();
		boolean hasntTripsAsAPassenger = user.getTripsAsAPassenger().isEmpty();
		
		if(!(hasntCars && hasntTripsAsAPassenger)) {
			throw new EntityWithDependenciesException("User", id);
		}
		
		userRepository.delete(user);
	}
	

}
