package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserUpdateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-05T19:22:42+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserUpdateMapperImpl implements UserUpdateMapper {

    @Override
    public void updateEntityFromDto(UserUpdateDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        entity.setEmail( dto.getEmail() );
        entity.setName( dto.getName() );
        entity.setPhone( dto.getPhone() );
        entity.setProfileImageUrl( dto.getProfileImageUrl() );
        entity.setUsername( dto.getUsername() );
    }
}
