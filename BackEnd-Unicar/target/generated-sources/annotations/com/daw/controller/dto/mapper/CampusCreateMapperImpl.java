package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-23T10:51:45+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CampusCreateMapperImpl implements CampusCreateMapper {

    @Override
    public CampusCreateDTO toDto(Campus campus) {
        if ( campus == null ) {
            return null;
        }

        CampusCreateDTO campusCreateDTO = new CampusCreateDTO();

        campusCreateDTO.setAddress( campus.getAddress() );
        campusCreateDTO.setName( campus.getName() );

        return campusCreateDTO;
    }

    @Override
    public Campus toEntity(CampusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Campus campus = new Campus();

        campus.setAddress( dto.getAddress() );
        campus.setName( dto.getName() );

        return campus;
    }
}
