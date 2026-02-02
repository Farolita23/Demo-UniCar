package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserDTO;
import com.daw.datamodel.entities.User;

public interface UserMapper {

	UserDTO toDto(User user);

}
