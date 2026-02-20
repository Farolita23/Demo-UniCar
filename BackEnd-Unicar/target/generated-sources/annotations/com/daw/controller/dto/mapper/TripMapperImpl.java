package com.daw.controller.dto.mapper;

import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.UserDTO;
import com.daw.datamodel.entities.Trip;
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
    date = "2026-02-20T12:30:29+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class TripMapperImpl implements TripMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CampusMapper campusMapper;
    @Autowired
    private TownMapper townMapper;

    @Override
    public TripDTO toDto(Trip trip) {
        if ( trip == null ) {
            return null;
        }

        TripDTO tripDTO = new TripDTO();

        tripDTO.setCarDTO( carMapper.toDto( trip.getCar() ) );
        tripDTO.setTownDTO( townMapper.toDto( trip.getTown() ) );
        tripDTO.setCampusDTO( campusMapper.toDto( trip.getCampus() ) );
        tripDTO.setPassengersDTO( userSetToUserDTOSet( trip.getPassengers() ) );
        tripDTO.setRequestersDTO( userSetToUserDTOSet( trip.getRequesters() ) );
        tripDTO.setDepartureAddress( trip.getDepartureAddress() );
        tripDTO.setDepartureDate( trip.getDepartureDate() );
        tripDTO.setDepartureTime( trip.getDepartureTime() );
        tripDTO.setId( trip.getId() );
        tripDTO.setIsToCampus( trip.getIsToCampus() );
        tripDTO.setPrice( trip.getPrice() );

        return tripDTO;
    }

    @Override
    public List<TripDTO> toListDto(List<Trip> trips) {
        if ( trips == null ) {
            return null;
        }

        List<TripDTO> list = new ArrayList<TripDTO>( trips.size() );
        for ( Trip trip : trips ) {
            list.add( toDto( trip ) );
        }

        return list;
    }

    protected Set<UserDTO> userSetToUserDTOSet(Set<User> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserDTO> set1 = new LinkedHashSet<UserDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( User user : set ) {
            set1.add( userMapper.toDto( user ) );
        }

        return set1;
    }
}
