package com.daw.controller.dto;

import java.time.LocalDate;

import com.daw.datamodel.entities.User;

import lombok.Data;

@Data
public class ReportDTO {
	
	private Long id;
	
	private String reason;
	
	private User userReport;
	
	private User reportedUser;
	
	private LocalDate date;

}
