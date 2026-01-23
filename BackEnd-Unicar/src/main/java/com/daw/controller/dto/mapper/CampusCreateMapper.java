package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;

@Mapper(componentModel = "spring")
public interface CampusCreateMapper {
	
	CampusCreateDTO toDto(Campus campus);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	Campus toEntity(CampusDTO dto);

}
