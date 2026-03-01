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
    date = "2026-03-01T11:43:33+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ReportDTO toDto(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setUserReportDTO( userMapper.toDto( report.getUserReport() ) );
        reportDTO.setReportedUserDTO( userMapper.toDto( report.getReportedUser() ) );
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
