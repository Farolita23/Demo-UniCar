package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.TownCreateDTO;
import com.daw.datamodel.entities.Town;

@Mapper(componentModel = "spring")
public interface TownCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	@Mapping(target = "users", ignore = true)
	Town toEntity(TownCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(TownCreateDTO dto, @MappingTarget Town entity);

}
