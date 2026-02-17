package com.daw.service;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.UserDTO;
import com.daw.controller.dto.mapper.UserCreateMapper;
import com.daw.controller.dto.mapper.UserMapper;
import com.daw.datamodel.repository.UserRepository;

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
	
	public UserDTO findById(Long id) {
		return userMapper.toDto(generalService.findUserById(id));
	}

}
