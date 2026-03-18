package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.daw.controller.dto.CarDTO;
import com.daw.datamodel.entities.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {
	
	CarDTO toDto(Car car);
	
	List<CarDTO> toListDto(List<Car> cars);

}
