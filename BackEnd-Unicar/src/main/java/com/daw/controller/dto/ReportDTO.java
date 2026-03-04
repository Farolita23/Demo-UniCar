package com.daw.controller.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReportDTO {
	
	private Long id;
	
	private String reason;
	
	private UserDTO userReportDTO;
	
	private UserDTO reportedUserDTO;
	
	private LocalDate date;

}
