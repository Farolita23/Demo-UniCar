package com.daw.controller.dto.mapper;

import com.daw.controller.dto.TownCreateDTO;
import com.daw.datamodel.entities.Town;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T20:44:09+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TownCreateMapperImpl implements TownCreateMapper {

    @Override
    public Town toEntity(TownCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Town town = new Town();

        town.setName( dto.getName() );
        if ( dto.getZipCode() != null ) {
            town.setZipCode( String.valueOf( dto.getZipCode() ) );
        }

        return town;
    }

    @Override
    public void updateEntityFromDto(TownCreateDTO dto, Town entity) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        if ( dto.getZipCode() != null ) {
            entity.setZipCode( String.valueOf( dto.getZipCode() ) );
        }
        else {
            entity.setZipCode( null );
        }
    }
}
