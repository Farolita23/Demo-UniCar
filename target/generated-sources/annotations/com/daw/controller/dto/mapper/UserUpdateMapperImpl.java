package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserUpdateDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T00:31:05+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserUpdateMapperImpl implements UserUpdateMapper {

    @Override
    public void updateEntityFromDto(UserUpdateDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        entity.setUsername( dto.getUsername() );
        entity.setEmail( dto.getEmail() );
        entity.setName( dto.getName() );
        entity.setPhone( dto.getPhone() );
        entity.setDrivingLicenseYear( dto.getDrivingLicenseYear() );
        entity.setProfileImageUrl( dto.getProfileImageUrl() );
        entity.setDescription( dto.getDescription() );
    }
}
