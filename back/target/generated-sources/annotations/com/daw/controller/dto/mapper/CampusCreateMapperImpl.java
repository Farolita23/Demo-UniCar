package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.datamodel.entities.Campus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-26T10:21:06+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CampusCreateMapperImpl implements CampusCreateMapper {

    @Override
    public Campus toEntity(CampusCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Campus campus = new Campus();

        campus.setAddress( dto.getAddress() );
        campus.setName( dto.getName() );

        return campus;
    }

    @Override
    public void updateEntityFromDto(CampusCreateDTO dto, Campus entity) {
        if ( dto == null ) {
            return;
        }

        entity.setAddress( dto.getAddress() );
        entity.setName( dto.getName() );
    }
}
