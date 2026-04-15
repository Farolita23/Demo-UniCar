package com.daw.controller.dto.mapper;

import com.daw.controller.dto.TripCreateDTO;
import com.daw.datamodel.entities.Trip;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T20:44:09+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TripCreateMapperImpl implements TripCreateMapper {

    @Override
    public Trip toEntity(TripCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Trip trip = new Trip();

        trip.setDepartureAddress( dto.getDepartureAddress() );
        trip.setDepartureDate( dto.getDepartureDate() );
        trip.setDepartureTime( dto.getDepartureTime() );
        trip.setIsToCampus( dto.getIsToCampus() );
        trip.setPrice( dto.getPrice() );

        return trip;
    }

    @Override
    public void updateEntityFromDto(TripCreateDTO dto, Trip entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDepartureAddress( dto.getDepartureAddress() );
        entity.setDepartureDate( dto.getDepartureDate() );
        entity.setDepartureTime( dto.getDepartureTime() );
        entity.setIsToCampus( dto.getIsToCampus() );
        entity.setPrice( dto.getPrice() );
    }
}
