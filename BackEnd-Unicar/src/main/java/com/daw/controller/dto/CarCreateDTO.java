package com.daw.controller.dto;

import com.daw.datamodel.entities.User;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarCreateDTO {
	
	@NotNull
	private User driver;
	
	@NotBlank
	private String model;
	
	@NotBlank
	private String color;
	
	@NotBlank
	private String licensePlate;
	
	@NotNull
	@Min(1)
	@Max(9)
	private Integer capacity;

}
