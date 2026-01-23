package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;

@Mapper(componentModel = "spring")
public interface CreateCarMapper {
	
	CarCreateDTO toDto(Car car);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	Car toEntity(CarCreateDTO dto);

}
