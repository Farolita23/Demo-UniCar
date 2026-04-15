package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;

import com.daw.controller.dto.UserSummaryDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring")
public interface UserSummaryMapper {

    UserSummaryDTO toDto(User user);

}
