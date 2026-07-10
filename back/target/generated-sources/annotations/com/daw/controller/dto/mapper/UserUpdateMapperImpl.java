package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserUpdateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-10T04:37:36+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
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
