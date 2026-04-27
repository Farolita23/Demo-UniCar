package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.daw.controller.dto.UserUpdateDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usualCampus", ignore = true)
    @Mapping(target = "homeTown", ignore = true)
    @Mapping(target = "reportsDone", ignore = true)
    @Mapping(target = "reportsReceived", ignore = true)
    @Mapping(target = "cars", ignore = true)
    @Mapping(target = "tripsAsAPassenger", ignore = true)
    @Mapping(target = "ratingsReceived", ignore = true)
    @Mapping(target = "ratingsDone", ignore = true)
    @Mapping(target = "banned", ignore = true)
    @Mapping(target = "strikes", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(UserUpdateDTO dto, @MappingTarget User entity);

}
