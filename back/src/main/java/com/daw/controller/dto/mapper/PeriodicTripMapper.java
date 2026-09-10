package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.PeriodicTripDTO;
import com.daw.datamodel.entities.PeriodicTrip;

@Mapper(componentModel = "spring", uses = {CarMapper.class, CampusMapper.class, TownMapper.class})
public interface PeriodicTripMapper {
	
	@Mapping(source = "car", target = "carDTO")
	@Mapping(source = "campus", target = "campusDTO")
	@Mapping(source = "town", target = "townDTO")
	PeriodicTripDTO toDto(PeriodicTrip entity);

}
