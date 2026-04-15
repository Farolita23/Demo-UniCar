package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.TripCreateDTO;
import com.daw.datamodel.entities.Trip;

@Mapper(componentModel = "spring")
public interface TripCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "car", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "town", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "campus", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "passengers", ignore = true) //Se crea vacío
	@Mapping(target = "requesters", ignore = true) //Se crea vacío
	Trip toEntity(TripCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(TripCreateDTO dto, @MappingTarget Trip entity);

}
