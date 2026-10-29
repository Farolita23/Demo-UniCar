package com.daw.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginDTO {

	/** ID token (JWT) emitido por Google Identity Services. */
	@NotBlank
	private String idToken;

}
