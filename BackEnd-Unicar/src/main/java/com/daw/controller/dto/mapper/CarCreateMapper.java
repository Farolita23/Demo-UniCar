package com.daw.controller.dto.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class CarCreateMapper {
	
	private final UserRepository userRepository;
		
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	@Mapping(target = "driver", source = "idDriver", qualifiedByName = "userById")
	public abstract Car toEntity(CarCreateDTO dto);
	
	@Named(value = "userById")
	public User userById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException(id);
		}
	}

}
