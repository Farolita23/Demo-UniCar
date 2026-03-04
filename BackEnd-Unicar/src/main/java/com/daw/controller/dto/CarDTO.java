package com.daw.controller.dto;

import lombok.Data;

@Data
public class CarDTO {
	
	private Long id;
	
	private UserDTO driverDTO;
	
	private String model;
	
	private String color;
	
	private String licensePlate;
	
	private Integer capacity;

}
