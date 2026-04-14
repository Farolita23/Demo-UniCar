package com.daw.controller.dto.mapper;

import com.daw.controller.dto.RatingDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.datamodel.entities.Rating;
import com.daw.datamodel.entities.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T20:44:09+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
        userDTO.setRatingsReceivedDTO( ratingSetToRatingDTOSet( user.getRatingsReceived() ) );
        userDTO.setRatingsDoneDTO( ratingSetToRatingDTOSet( user.getRatingsDone() ) );
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

    protected Set<RatingDTO> ratingSetToRatingDTOSet(Set<Rating> set) {
        if ( set == null ) {
            return null;
        }

        Set<RatingDTO> set1 = new LinkedHashSet<RatingDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Rating rating : set ) {
            set1.add( ratingMapper.toDto( rating ) );
        }

        return set1;
    }
}
