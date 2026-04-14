package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportDTO;
import com.daw.datamodel.entities.Report;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T20:44:09+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Override
    public ReportDTO toDto(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setDate( report.getDate() );
        reportDTO.setId( report.getId() );
        reportDTO.setReason( report.getReason() );

        return reportDTO;
    }

    @Override
    public List<ReportDTO> toListDto(List<Report> reports) {
        if ( reports == null ) {
            return null;
        }

        List<ReportDTO> list = new ArrayList<ReportDTO>( reports.size() );
        for ( Report report : reports ) {
            list.add( toDto( report ) );
        }

        return list;
    }
}
