package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusCreateDTO;
import com.daw.datamodel.entities.Campus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:26+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
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
