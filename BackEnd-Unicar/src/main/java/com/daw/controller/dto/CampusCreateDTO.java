package com.daw.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CampusCreateDTO {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String address;

}
