package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-23T11:00:21+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CampusMapperImpl implements CampusMapper {

    @Override
    public CampusDTO toDto(Campus campus) {
        if ( campus == null ) {
            return null;
        }

        CampusDTO campusDTO = new CampusDTO();

        campusDTO.setAddress( campus.getAddress() );
        campusDTO.setId( campus.getId() );
        campusDTO.setName( campus.getName() );

        return campusDTO;
    }

    @Override
    public List<CampusDTO> toListDto(List<Campus> campus) {
        if ( campus == null ) {
            return null;
        }

        List<CampusDTO> list = new ArrayList<CampusDTO>( campus.size() );
        for ( Campus campus1 : campus ) {
            list.add( toDto( campus1 ) );
        }

        return list;
    }
}
