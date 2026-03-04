package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.datamodel.entities.Campus;

@Mapper(componentModel = "spring")
public interface CampusCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	@Mapping(target = "users", ignore = true)
	Campus toEntity(CampusCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(CampusCreateDTO dto, @MappingTarget Campus entity);

}
