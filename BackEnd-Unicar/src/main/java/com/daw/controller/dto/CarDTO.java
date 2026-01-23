package com.daw.controller.dto;

import com.daw.datamodel.entities.User;

import lombok.Data;

@Data
public class CarDTO {
	
	private Long id;
	
	private User driver;
	
	private String model;
	
	private String color;
	
	private String licensePlate;
	
	private Integer capacity;

}
