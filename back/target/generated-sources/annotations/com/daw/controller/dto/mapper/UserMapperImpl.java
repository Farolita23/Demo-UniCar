package com.daw.controller.dto.mapper;

import com.daw.controller.dto.UserDTO;
import com.daw.datamodel.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T17:25:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private CampusMapper campusMapper;
    @Autowired
    private TownMapper townMapper;
    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUsualCampusDTO( campusMapper.toDto( user.getUsualCampus() ) );
        userDTO.setHomeTownDTO( townMapper.toDto( user.getHomeTown() ) );
        userDTO.setRatingsReceivedDTO( ratingMapper.toSetDto( user.getRatingsReceived() ) );
        userDTO.setRatingsDoneDTO( ratingMapper.toSetDto( user.getRatingsDone() ) );
        userDTO.setBanned( user.getBanned() );
        userDTO.setBirthdate( user.getBirthdate() );
        userDTO.setDescription( user.getDescription() );
        userDTO.setDrivingLicenseYear( user.getDrivingLicenseYear() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setGenre( user.getGenre() );
        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );
        userDTO.setPhone( user.getPhone() );
        userDTO.setProfileImageUrl( user.getProfileImageUrl() );
        userDTO.setRole( user.getRole() );
        userDTO.setStrikes( user.getStrikes() );
        userDTO.setUsername( user.getUsername() );

        return userDTO;
    }

    @Override
    public List<UserDTO> toListDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }
}
