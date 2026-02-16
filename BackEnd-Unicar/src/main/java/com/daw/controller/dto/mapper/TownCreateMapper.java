package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.TownCreateDTO;
import com.daw.datamodel.entities.Town;

@Mapper(componentModel = "spring")
public interface TownCreateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	Town toEntity(TownCreateDTO dto);

}
