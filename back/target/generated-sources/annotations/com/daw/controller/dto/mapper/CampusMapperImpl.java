package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-10T04:37:35+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
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
