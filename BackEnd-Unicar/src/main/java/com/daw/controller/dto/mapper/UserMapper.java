package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.UserDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring", uses = {CarMapper.class,
		   								   CampusMapper.class,
		   								   TownMapper.class,
		   								   ReportMapper.class,
		   								   RatingMapper.class})
public interface UserMapper {

	@Mapping(target = "usualCampusDTO", source = "usualCampus")
	@Mapping(target = "homeTownDTO", source = "homeTown")
	@Mapping(target = "reportsDoneDTO", source = "reportsDone")
	@Mapping(target = "reportsReceivedDTO", source = "reportsReceived")
	@Mapping(target = "carsDTO", source = "cars")
	@Mapping(target = "ratingsReceivedDTO", source = "ratingsReceived")
	@Mapping(target = "ratingsDoneDTO", source = "ratingsDone")
	UserDTO toDto(User user);
	
	List<UserDTO> toListDto(List<User> users);

}
