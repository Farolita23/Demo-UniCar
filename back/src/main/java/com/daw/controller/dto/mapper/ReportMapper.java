package com.daw.controller.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.daw.controller.dto.ReportDTO;
import com.daw.datamodel.entities.Report;

@Mapper(componentModel = "spring", uses = {UserSummaryMapper.class})
public interface ReportMapper {

    @Mapping(target = "userReportDTO", source = "userReport")
    @Mapping(target = "reportedUserDTO", source = "reportedUser")
    ReportDTO toDto(Report report);

    List<ReportDTO> toListDto(List<Report> reports);

    default Page<ReportDTO> toPageDto(Page<Report> reports) {
        return reports.map(this::toDto);
    }
}
