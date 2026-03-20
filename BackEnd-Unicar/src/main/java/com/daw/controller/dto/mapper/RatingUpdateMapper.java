package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.RatingUpdateDTO;
import com.daw.datamodel.entities.Rating;

@Mapper(componentModel = "spring")
public interface RatingUpdateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userRate", ignore = true)
	@Mapping(target = "ratedUser", ignore = true)
	void updateEntityFromDto(RatingUpdateDTO dto, @MappingTarget Rating entity);

}
