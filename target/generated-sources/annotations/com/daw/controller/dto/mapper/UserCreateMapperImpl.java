package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserCreateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T00:31:05+0200",
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

        user.setUsername( dto.getUsername() );
        user.setEmail( dto.getEmail() );
        user.setName( dto.getName() );
        user.setBirthdate( dto.getBirthdate() );
        user.setGenre( dto.getGenre() );
        user.setPhone( dto.getPhone() );
        user.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        user.setPassword( dto.getPassword() );
        user.setProfileImageUrl( dto.getProfileImageUrl() );
        user.setDescription( dto.getDescription() );

        return user;
    }

    @Override
    public void updateEntityFromDto(UserCreateDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setUsername( dto.getUsername() );
        entity.setEmail( dto.getEmail() );
        entity.setName( dto.getName() );
        entity.setBirthdate( dto.getBirthdate() );
        entity.setGenre( dto.getGenre() );
        entity.setPhone( dto.getPhone() );
        entity.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        entity.setPassword( dto.getPassword() );
        entity.setProfileImageUrl( dto.getProfileImageUrl() );
        entity.setDescription( dto.getDescription() );
    }
}
