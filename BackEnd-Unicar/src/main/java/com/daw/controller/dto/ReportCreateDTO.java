package com.daw.controller.dto;

import java.time.LocalDate;

import com.daw.datamodel.entities.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportCreateDTO {
	
	@NotBlank
	private String reason;
	
	@NotNull
	private User userReport;
	
	@NotNull
	private User reportedUser;
	
	@NotNull
	private LocalDate date;

}
