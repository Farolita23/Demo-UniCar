package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportUpdateDTO;
import com.daw.datamodel.entities.Report;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T13:37:18+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReportUpdateMapperImpl implements ReportUpdateMapper {

    @Override
    public void updateEntityFromDto(ReportUpdateDTO dto, Report entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDate( dto.getDate() );
        entity.setReason( dto.getReason() );
    }
}
