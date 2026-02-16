package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.daw.controller.dto.TownDTO;
import com.daw.datamodel.entities.Town;

@Mapper(componentModel = "spring")
public interface TownMapper {
	
	TownDTO toDto(Town town);
	
	List<TownDTO> toListDto(List<Town> towns);

}
