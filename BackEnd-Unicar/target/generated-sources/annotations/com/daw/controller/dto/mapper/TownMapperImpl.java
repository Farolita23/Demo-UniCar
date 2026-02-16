package com.daw.controller.dto.mapper;

import com.daw.controller.dto.TownDTO;
import com.daw.datamodel.entities.Town;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T11:03:28+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class TownMapperImpl implements TownMapper {

    @Override
    public TownDTO toDto(Town town) {
        if ( town == null ) {
            return null;
        }

        TownDTO townDTO = new TownDTO();

        townDTO.setId( town.getId() );
        townDTO.setName( town.getName() );
        if ( town.getZipCode() != null ) {
            townDTO.setZipCode( Integer.parseInt( town.getZipCode() ) );
        }

        return townDTO;
    }

    @Override
    public List<TownDTO> toListDto(List<Town> towns) {
        if ( towns == null ) {
            return null;
        }

        List<TownDTO> list = new ArrayList<TownDTO>( towns.size() );
        for ( Town town : towns ) {
            list.add( toDto( town ) );
        }

        return list;
    }
}
