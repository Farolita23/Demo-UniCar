package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T17:25:26+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
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
        user.setProfileImageUrl( dto.getProfileImageUrl() );
        user.setUsername( dto.getUsername() );

        return user;
    }

    @Override
    public void updateEntityFromDto(UserCreateDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setBirthdate( dto.getBirthdate() );
        entity.setDescription( dto.getDescription() );
        entity.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        entity.setEmail( dto.getEmail() );
        entity.setGenre( dto.getGenre() );
        entity.setName( dto.getName() );
        entity.setPassword( dto.getPassword() );
        entity.setPhone( dto.getPhone() );
        entity.setProfileImageUrl( dto.getProfileImageUrl() );
        entity.setUsername( dto.getUsername() );
    }
}
