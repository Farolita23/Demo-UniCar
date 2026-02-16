package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T11:03:28+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserCreateMapperImpl implements UserCreateMapper {

    @Override
    public User toEntity(UserCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setBirthdate( dto.getBirthdate() );
        user.setDescription( dto.getDescription() );
        user.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        user.setEmail( dto.getEmail() );
        user.setGenre( dto.getGenre() );
        user.setName( dto.getName() );
        user.setPassword( dto.getPassword() );
        user.setPhone( dto.getPhone() );
        user.setUsername( dto.getUsername() );

        return user;
    }
}
