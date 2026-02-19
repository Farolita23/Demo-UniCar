package com.daw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.controller.dto.ReportCreateDTO;
import com.daw.controller.dto.ReportDTO;
import com.daw.controller.dto.ReportUpdateDTO;
import com.daw.controller.dto.mapper.ReportCreateMapper;
import com.daw.controller.dto.mapper.ReportMapper;
import com.daw.controller.dto.mapper.ReportUpdateMapper;
import com.daw.datamodel.entities.Report;
import com.daw.datamodel.repository.ReportRepository;
import com.daw.exceptions.InvalidReportException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportRepository reportRepository;
	private final ReportMapper reportMapper;
	private final ReportCreateMapper reportCreateMapper;
	private final ReportUpdateMapper reportUpdateMapper;
	private final GeneralService generalService;
	
	public List<ReportDTO> findAll() {
		return reportMapper.toListDto(reportRepository.findAll());
	}
	
	public ReportDTO findById(Long id) {
		return reportMapper.toDto(generalService.findReportById(id));
	}
	
	public ReportDTO create(ReportCreateDTO dto) {
		Long idUserReport = dto.getIdUserReport();
		Long idReportedUser = dto.getIdReportedUser();
		
		if(idUserReport.equals(idReportedUser) || !generalService.existsSharedTrip(idUserReport, idReportedUser)) {
			throw new InvalidReportException(idUserReport, idReportedUser);
		}
		
		Report report = reportCreateMapper.toEntity(dto);
		report.setUserReport(generalService.findUserById(idUserReport));
		report.setReportedUser(generalService.findUserById(idReportedUser));
		
		reportRepository.save(report);
		return reportMapper.toDto(report);
	}
	
	public ReportDTO update(ReportUpdateDTO dto, Long id) {
		Report report = generalService.findReportById(id);
		reportUpdateMapper.updateEntityFromDto(dto, report);
		Report updatedReport = reportRepository.save(report);
		return reportMapper.toDto(updatedReport);
	}
	
	public void delete(Long id) {
		reportRepository.delete(generalService.findReportById(id));
	}

}
