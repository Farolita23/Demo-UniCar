package com.daw.controller.dto.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.datamodel.entities.Rating;
import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;
import com.daw.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class RatingCreateMapper {
	
	private final UserRepository userRepository;
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userRate", source = "idUserRate", qualifiedByName = "userById")
	@Mapping(target = "ratedUser", source = "idRatedUser", qualifiedByName = "userById")
	public abstract Rating toEntity(RatingCreateDTO dto);
	
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
