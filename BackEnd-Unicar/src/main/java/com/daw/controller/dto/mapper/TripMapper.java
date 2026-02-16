package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.TripDTO;
import com.daw.datamodel.entities.Trip;

@Mapper(componentModel = "spring", uses = {UserMapper.class,
										   CarMapper.class,
										   CampusMapper.class,
										   TownMapper.class})
public interface TripMapper {
	
	@Mapping(target = "carDTO", source = "car")
	@Mapping(target = "townDTO", source = "town")
	@Mapping(target = "campusDTO", source = "campus")
	@Mapping(target = "passengersDTO", source = "passengers")
	TripDTO toDto(Trip trip);
	
	List<TripDTO> toListDto(List<Trip> trips);

}
