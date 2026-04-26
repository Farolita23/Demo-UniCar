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

/**
 * Servicio encargado de gestionar las operaciones de negocio relacionadas con los reportes de usuarios.
 *
 * Controla el ciclo de vida de los reportes aplicando las reglas del dominio:
 * un usuario solo puede reportar a otro si han compartido un viaje y no puede
 * reportarse a sí mismo. La consulta y gestión de reportes está reservada al
 * rol de administrador.
 *
 * @author Javier y Adam
 * @version 1.0.0
 * @see ReportRepository
 * @see ReportMapper
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

	private final ReportRepository reportRepository;
	private final ReportMapper reportMapper;
	private final ReportCreateMapper reportCreateMapper;
	private final ReportUpdateMapper reportUpdateMapper;
	private final GeneralService generalService;

	/**
	 * Recupera todos los reportes registrados en el sistema.
	 *
	 * @return lista de {@link ReportDTO} con todos los reportes disponibles
	 */
	public List<ReportDTO> findAll() {
		return reportMapper.toListDto(reportRepository.findAll());
	}

	/**
	 * Recupera un reporte por su identificador.
	 *
	 * @param id identificador del reporte
	 * @return {@link ReportDTO} con los datos del reporte encontrado
	 * @throws com.daw.exceptions.ReportNotFoundException si no existe un reporte con ese identificador
	 */
	public ReportDTO findById(Long id) {
		return reportMapper.toDto(generalService.findReportById(id));
	}

	/**
	 * Crea un nuevo reporte verificando las reglas de negocio del dominio.
	 *
	 * @param dto datos del reporte a crear
	 * @return {@link ReportDTO} con los datos del reporte recién creado
	 * @throws InvalidReportException si el emisor intenta reportarse a sí mismo o no comparte viaje con el reportado
	 */
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

	/**
	 * Actualiza el motivo y la fecha de un reporte existente.
	 *
	 * Los usuarios implicados son inmutables una vez creado el reporte.
	 *
	 * @param dto datos actualizados del reporte
	 * @param id  identificador del reporte a modificar
	 * @return {@link ReportDTO} con los datos del reporte actualizado
	 * @throws com.daw.exceptions.ReportNotFoundException si no existe un reporte con ese identificador
	 */
	public ReportDTO update(ReportUpdateDTO dto, Long id) {
		Report report = generalService.findReportById(id);
		reportUpdateMapper.updateEntityFromDto(dto, report);
		Report updatedReport = reportRepository.save(report);
		return reportMapper.toDto(updatedReport);
	}

	/**
	 * Elimina un reporte por su identificador.
	 *
	 * @param id identificador del reporte a eliminar
	 * @throws com.daw.exceptions.ReportNotFoundException si no existe un reporte con ese identificador
	 */
	public void delete(Long id) {
		reportRepository.delete(generalService.findReportById(id));
	}

}
