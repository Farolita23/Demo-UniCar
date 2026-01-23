package com.daw.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.Car;
import com.daw.datamodel.entities.Town;
import com.daw.datamodel.entities.User;

import lombok.Data;

@Data
public class TripDTO {
	
	private Long id;
	
	private Car car;
	
	private Campus campus;
	
	private Town town;
	
	private Boolean isToCampus;
	
	private LocalDate departureDate;
	
	private LocalTime departureTime;
	
	private String departureAddress;
	
	private BigDecimal price;
	
	private Set<User> passengers;
}
