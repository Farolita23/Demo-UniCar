package com.daw.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TownCreateDTO {
	
	@NotBlank
	private String name;
	
	@NotNull
	private Integer zipCode;

}
