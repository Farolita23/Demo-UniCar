package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring")
public interface UserCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "usualCampus", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "homeTown", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "reportsDone", ignore = true) //Se crea vacío
	@Mapping(target = "reportsReceived", ignore = true) //Se crea vacío
	@Mapping(target = "cars", ignore = true) //Se crea vacío
	@Mapping(target = "tripsAsAPassenger", ignore = true) //Se crea vacío
	@Mapping(target = "ratingsReceived", ignore = true) //Se crea vacío
	@Mapping(target = "ratingsDone", ignore = true) //Se crea vacío
	@Mapping(target = "banned", ignore = true) //Habrá que crearlo en el servicio (como false en principio)
	@Mapping(target = "strikes", ignore = true) //Habrá que crearlo en el servicio (como 0 en principio)
	User toEntity(UserCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(UserCreateDTO dto, @MappingTarget User entity);

}
