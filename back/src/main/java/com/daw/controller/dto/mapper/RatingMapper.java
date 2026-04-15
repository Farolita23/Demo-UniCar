package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.daw.controller.dto.RatingDTO;
import com.daw.datamodel.entities.Rating;

@Mapper(componentModel = "spring")
public interface RatingMapper {
	
	RatingDTO toDto(Rating rating);
	
	List<RatingDTO> toListDto(List<Rating> ratings);

}
