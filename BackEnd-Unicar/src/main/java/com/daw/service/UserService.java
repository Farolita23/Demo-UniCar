package com.daw.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.mapper.UserMapper;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	public User findEntityById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException(id);
		}
	}
	
	public UserDTO findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return userMapper.toDto(user.get());
		} else {
			throw new UserNotFoundException(id);
		}
	}

}
