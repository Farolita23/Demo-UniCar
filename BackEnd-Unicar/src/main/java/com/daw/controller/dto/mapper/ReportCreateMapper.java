package com.daw.controller.dto.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.datamodel.entities.Report;
import com.daw.datamodel.entities.User;
import com.daw.exceptions.UserNotFoundException;
import com.daw.service.UserService;

import lombok.RequiredArgsConstructor;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class ReportCreateMapper {
	
	private final UserService userService;
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userReport", source = "idUserReport", qualifiedByName = "userById")
	@Mapping(target = "reportedUser", source = "idReportedUser", qualifiedByName = "userById")
	public abstract Report toEntity(ReportCreateDTO dto);
	
	@Named(value = "userById")
	public User userById(Long id) {
		return userService.findEntityById(id);
	}

}
