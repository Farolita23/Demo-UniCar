package com.daw.controller.dto.mapper;

import com.daw.controller.dto.WarningDTO;
import com.daw.datamodel.entities.Warning;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T13:37:18+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class WarningMapperImpl implements WarningMapper {

    @Autowired
    private UserSummaryMapper userSummaryMapper;

    @Override
    public WarningDTO toDto(Warning warning) {
        if ( warning == null ) {
            return null;
        }

        WarningDTO warningDTO = new WarningDTO();

        warningDTO.setUserDTO( userSummaryMapper.toDto( warning.getUser() ) );
        warningDTO.setAdminDTO( userSummaryMapper.toDto( warning.getAdmin() ) );
        warningDTO.setCreatedAt( warning.getCreatedAt() );
        warningDTO.setId( warning.getId() );
        warningDTO.setIsRead( warning.getIsRead() );
        warningDTO.setMessage( warning.getMessage() );
        warningDTO.setSubject( warning.getSubject() );

        return warningDTO;
    }

    @Override
    public List<WarningDTO> toListDto(List<Warning> warnings) {
        if ( warnings == null ) {
            return null;
        }

        List<WarningDTO> list = new ArrayList<WarningDTO>( warnings.size() );
        for ( Warning warning : warnings ) {
            list.add( toDto( warning ) );
        }

        return list;
    }
}
