package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.Town;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripCreateDTO {
	
	@NotNull
	private Car car;
	
	@NotNull
	private Campus campus;
	
	@NotNull
	private Town town;
	
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
