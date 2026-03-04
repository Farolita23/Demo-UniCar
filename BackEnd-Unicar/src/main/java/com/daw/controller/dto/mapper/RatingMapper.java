package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.RatingDTO;
import com.daw.datamodel.entities.Rating;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RatingMapper {
	
	@Mapping(target = "userRateDTO", source = "userRate")
	@Mapping(target = "ratedUserDTO", source = "ratedUser")
	RatingDTO toDto(Rating rating);
	
	List<RatingDTO> toListDto(List<Rating> ratings);

}
