package com.daw.controller.dto.mapper;

import com.daw.controller.dto.TripDTO;
import com.daw.controller.dto.UserSummaryDTO;
import com.daw.datamodel.entities.Car;
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
    date = "2026-04-16T09:46:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class TripMapperImpl implements TripMapper {

    @Autowired
    private UserSummaryMapper userSummaryMapper;
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
        tripDTO.setDriverDTO( userSummaryMapper.toDto( tripCarDriver( trip ) ) );
        tripDTO.setPassengersDTO( userSetToUserSummaryDTOSet( trip.getPassengers() ) );
        tripDTO.setRequestersDTO( userSetToUserSummaryDTOSet( trip.getRequesters() ) );
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

    private User tripCarDriver(Trip trip) {
        Car car = trip.getCar();
        if ( car == null ) {
            return null;
        }
        return car.getDriver();
    }

    protected Set<UserSummaryDTO> userSetToUserSummaryDTOSet(Set<User> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserSummaryDTO> set1 = new LinkedHashSet<UserSummaryDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( User user : set ) {
            set1.add( userSummaryMapper.toDto( user ) );
        }

        return set1;
    }
}
