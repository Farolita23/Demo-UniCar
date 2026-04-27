package com.daw.controller.dto.mapper;

import com.daw.controller.dto.CampusDTO;
import com.daw.datamodel.entities.Campus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T10:23:54+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
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
