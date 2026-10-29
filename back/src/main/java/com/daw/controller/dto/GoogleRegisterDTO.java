package com.daw.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Datos de perfil que completa un usuario nuevo tras autenticarse con Google.
 * El correo y el nombre se toman del ID token, no de este DTO.
 */
@Data
public class GoogleRegisterDTO {

	@NotBlank
	private String idToken;

	@NotBlank
	private String username;

	/** Nombre a mostrar; si viene vacío se usa el de la cuenta de Google. */
	private String name;

	@NotNull
	private LocalDate birthdate;

	@NotBlank
	private String genre;

	@NotNull
	private String phone;

	private Integer drivingLicenseYear;

	@NotNull
	private Long idUsualCampus;

	@NotNull
	private Long idHomeTown;

	private String description;

}
