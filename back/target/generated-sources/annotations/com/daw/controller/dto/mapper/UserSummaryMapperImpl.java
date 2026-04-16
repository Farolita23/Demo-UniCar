package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserSummaryDTO;
import com.daw.datamodel.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:25+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class UserSummaryMapperImpl implements UserSummaryMapper {

    @Override
    public UserSummaryDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserSummaryDTO userSummaryDTO = new UserSummaryDTO();

        userSummaryDTO.setId( user.getId() );
        userSummaryDTO.setName( user.getName() );
        userSummaryDTO.setProfileImageUrl( user.getProfileImageUrl() );
        userSummaryDTO.setUsername( user.getUsername() );

        return userSummaryDTO;
    }
}
