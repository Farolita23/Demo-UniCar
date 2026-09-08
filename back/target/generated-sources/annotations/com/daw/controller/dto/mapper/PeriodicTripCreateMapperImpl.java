package com.daw.controller.dto.mapper;

import com.daw.controller.dto.PeriodicTripCreateDTO;
import com.daw.datamodel.entities.PeriodicTrip;
import java.time.DayOfWeek;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-08T15:14:56+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PeriodicTripCreateMapperImpl implements PeriodicTripCreateMapper {

    @Override
    public PeriodicTrip toEntity(PeriodicTripCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PeriodicTrip periodicTrip = new PeriodicTrip();

        Set<DayOfWeek> set = dto.getDaysOfWeek();
        if ( set != null ) {
            periodicTrip.setDaysOfWeek( new LinkedHashSet<DayOfWeek>( set ) );
        }
        periodicTrip.setDepartureAddress( dto.getDepartureAddress() );
        periodicTrip.setDepartureTime( dto.getDepartureTime() );
        periodicTrip.setEndDate( dto.getEndDate() );
        periodicTrip.setIsToCampus( dto.getIsToCampus() );
        periodicTrip.setPrice( dto.getPrice() );
        periodicTrip.setRepeatIntervalWeeks( dto.getRepeatIntervalWeeks() );
        periodicTrip.setStartDate( dto.getStartDate() );

        return periodicTrip;
    }

    @Override
    public void updateEntityFromDto(PeriodicTripCreateDTO dto, PeriodicTrip entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getDaysOfWeek() != null ) {
            Set<DayOfWeek> set = dto.getDaysOfWeek();
            if ( set != null ) {
                entity.getDaysOfWeek().clear();
                entity.getDaysOfWeek().addAll( set );
            }
            else {
                entity.setDaysOfWeek( null );
            }
        }
        else {
            Set<DayOfWeek> set = dto.getDaysOfWeek();
            if ( set != null ) {
                entity.setDaysOfWeek( new LinkedHashSet<DayOfWeek>( set ) );
            }
        }
        entity.setDepartureAddress( dto.getDepartureAddress() );
        entity.setDepartureTime( dto.getDepartureTime() );
        entity.setEndDate( dto.getEndDate() );
        entity.setIsToCampus( dto.getIsToCampus() );
        entity.setPrice( dto.getPrice() );
        entity.setRepeatIntervalWeeks( dto.getRepeatIntervalWeeks() );
        entity.setStartDate( dto.getStartDate() );
    }
}
