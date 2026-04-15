package com.daw.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportUpdateDTO {
	
	@NotBlank
	private String reason;
	
	@NotNull
	private LocalDate date;

}
