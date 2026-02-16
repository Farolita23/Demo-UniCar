package com.daw.controller.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.CarCreateDTO;
import com.daw.datamodel.entities.Car;

@Mapper(componentModel = "spring")
public interface CarCreateMapper {
			
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trips", ignore = true)
	@Mapping(target = "driver", ignore = true) //Habrá que crearlo en el servicio
	public abstract Car toEntity(CarCreateDTO dto);

}
