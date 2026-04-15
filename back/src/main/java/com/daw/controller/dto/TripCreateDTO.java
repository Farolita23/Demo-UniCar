package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripCreateDTO {
	
	@NotNull
	private Long idCar;
	
	@NotNull
	private Long idCampus;
	
	@NotNull
	private Long idTown;
	
	@NotNull
	private Boolean isToCampus;
	
	@NotNull
	private LocalDate departureDate;
	
	@NotNull
	private LocalTime departureTime;
	
	@NotBlank
	private String departureAddress;
	
	@NotNull
	private BigDecimal price;

}
