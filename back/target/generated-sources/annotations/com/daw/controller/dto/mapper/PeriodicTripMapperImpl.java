package com.daw.controller.dto.mapper;

import com.daw.controller.dto.PeriodicTripDTO;
import com.daw.datamodel.entities.PeriodicTrip;
import java.time.DayOfWeek;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-10T04:37:36+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PeriodicTripMapperImpl implements PeriodicTripMapper {

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CampusMapper campusMapper;
    @Autowired
    private TownMapper townMapper;

    @Override
    public PeriodicTripDTO toDto(PeriodicTrip entity) {
        if ( entity == null ) {
            return null;
        }

        PeriodicTripDTO periodicTripDTO = new PeriodicTripDTO();

        periodicTripDTO.setCarDTO( carMapper.toDto( entity.getCar() ) );
        periodicTripDTO.setCampusDTO( campusMapper.toDto( entity.getCampus() ) );
        periodicTripDTO.setTownDTO( townMapper.toDto( entity.getTown() ) );
        Set<DayOfWeek> set = entity.getDaysOfWeek();
        if ( set != null ) {
            periodicTripDTO.setDaysOfWeek( new LinkedHashSet<DayOfWeek>( set ) );
        }
        periodicTripDTO.setDepartureAddress( entity.getDepartureAddress() );
        periodicTripDTO.setDepartureTime( entity.getDepartureTime() );
        periodicTripDTO.setEndDate( entity.getEndDate() );
        periodicTripDTO.setId( entity.getId() );
        periodicTripDTO.setIsToCampus( entity.getIsToCampus() );
        periodicTripDTO.setPrice( entity.getPrice() );
        periodicTripDTO.setRepeatIntervalWeeks( entity.getRepeatIntervalWeeks() );
        periodicTripDTO.setStartDate( entity.getStartDate() );

        return periodicTripDTO;
    }
}
