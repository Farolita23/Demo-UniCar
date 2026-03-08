package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.daw.controller.dto.ReportDTO;
import com.daw.datamodel.entities.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {
	
	ReportDTO toDto(Report report);
	
	List<ReportDTO> toListDto(List<Report> reports);

}
