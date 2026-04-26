package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.RatingCreateDTO;
import com.daw.datamodel.entities.Rating;

@Mapper(componentModel = "spring")
public interface RatingCreateMapper {
		
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userRate", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "ratedUser", ignore = true) //Habrá que crearlo en el servicio
	Rating toEntity(RatingCreateDTO dto);

}
