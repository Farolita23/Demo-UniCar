package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.datamodel.entities.Report;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-16T09:46:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 25.0.1 (Eclipse Adoptium)"
)
@Component
public class ReportCreateMapperImpl implements ReportCreateMapper {

    @Override
    public Report toEntity(ReportCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Report report = new Report();

        report.setDate( dto.getDate() );
        report.setReason( dto.getReason() );

        return report;
    }
}
