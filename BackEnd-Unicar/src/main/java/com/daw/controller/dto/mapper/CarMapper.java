package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.CarDTO;
import com.daw.datamodel.entities.Car;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CarMapper {
	
	@Mapping(target = "driverDTO", source = "driver")
	CarDTO toDto(Car car);
	
	List<CarDTO> toListDto(List<Car> cars);

}
