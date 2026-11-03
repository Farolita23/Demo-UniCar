package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.daw.controller.dto.UserSummaryDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring")
public interface UserSummaryMapper {

    @Mapping(target = "avgRating", ignore = true)
    @Mapping(target = "ratingsCount", ignore = true)
    UserSummaryDTO toDto(User user);

}
