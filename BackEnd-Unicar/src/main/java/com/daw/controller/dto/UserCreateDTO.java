package com.daw.controller.dto;

import java.time.LocalDate;

import com.daw.datamodel.entities.Campus;
import com.daw.datamodel.entities.Town;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateDTO {
	
	@Email
	private String email;
	
	@Pattern(regexp = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[!@#$%^&?])[A-Za-z\\d!@#$%^&?]{8,}$")
	private String password;
	
	@NotBlank
	private String name;
	
	@NotNull
	private LocalDate birthdate;
	
	@NotBlank
	private String genre;
	
	@NotNull
	private String phone;
	
	private Integer drivingLicenseYear;
	
	@NotNull
	private Campus usualCampus;
	
	@NotNull
	private Town homeTown;

    private String description;

}
