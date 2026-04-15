package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;

@Mapper(componentModel = "spring")
public interface CampusMapper {
	
	CampusDTO toDto(Campus campus);
	
	List<CampusDTO> toListDto(List<Campus> campus);

}
