package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1064475690647727844L;

	public CarNotFoundException(Long id) {
		super("No existe el coche con id: " + id);
	}

}
