package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.datamodel.entities.Report;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T00:31:05+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReportCreateMapperImpl implements ReportCreateMapper {

    @Override
    public Report toEntity(ReportCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Report report = new Report();

        report.setReason( dto.getReason() );
        report.setDate( dto.getDate() );

        return report;
    }
}
