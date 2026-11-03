package com.daw.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.daw.controller.dto.UserListDTO;
import com.daw.datamodel.entities.User;

@Mapper(componentModel = "spring", uses = {CampusMapper.class, TownMapper.class})
public interface UserListMapper {

    @Mapping(target = "usualCampusDTO", source = "usualCampus")
    @Mapping(target = "homeTownDTO",    source = "homeTown")
    UserListDTO toDto(User user);

    default Page<UserListDTO> toPageDto(Page<User> users) {
        return users.map(this::toDto);
    }
}
