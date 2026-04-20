package com.daw.controller.dto.mapper;

import com.daw.controller.dto.ReportDTO;
import com.daw.datamodel.entities.Report;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T17:25:27+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Autowired
    private UserSummaryMapper userSummaryMapper;

    @Override
    public ReportDTO toDto(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setUserReportDTO( userSummaryMapper.toDto( report.getUserReport() ) );
        reportDTO.setReportedUserDTO( userSummaryMapper.toDto( report.getReportedUser() ) );
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
