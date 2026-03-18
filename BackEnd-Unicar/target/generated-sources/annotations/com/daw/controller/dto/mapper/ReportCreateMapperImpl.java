package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.datamodel.entities.Report;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-08T11:19:57+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
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
