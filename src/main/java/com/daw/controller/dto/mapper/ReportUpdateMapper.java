package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.ReportUpdateDTO;
import com.daw.datamodel.entities.Report;

@Mapper(componentModel = "spring")
public interface ReportUpdateMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userReport", ignore = true)
	@Mapping(target = "reportedUser", ignore = true)
	void updateEntityFromDto(ReportUpdateDTO dto, @MappingTarget Report entity);

}
