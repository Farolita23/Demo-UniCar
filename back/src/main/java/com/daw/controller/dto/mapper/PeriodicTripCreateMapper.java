package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.PeriodicTripCreateDTO;
import com.daw.datamodel.entities.PeriodicTrip;

@Mapper(componentModel = "spring")
public interface PeriodicTripCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "car", ignore = true)
	@Mapping(target = "town", ignore = true)
	@Mapping(target = "campus", ignore = true)
	PeriodicTrip toEntity(PeriodicTripCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(PeriodicTripCreateDTO dto, @MappingTarget PeriodicTrip entity);

}
