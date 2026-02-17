package com.daw.controller.dto.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.datamodel.entities.Report;

@Mapper(componentModel = "spring")
public interface ReportCreateMapper {
		
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userReport", ignore = true) //Habrá que crearlo en el servicio
	@Mapping(target = "reportedUser", ignore = true) //Habrá que crearlo en el servicio
	Report toEntity(ReportCreateDTO dto);
	
	@InheritConfiguration
	void updateEntityFromDto(ReportCreateDTO dto, @MappingTarget Report entity);

}
