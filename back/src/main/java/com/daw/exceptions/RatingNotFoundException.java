package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RatingNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -2176725443014114851L;

	public RatingNotFoundException(Long id) {
		super("No existe la valoración con id: " + id);
	}

}
