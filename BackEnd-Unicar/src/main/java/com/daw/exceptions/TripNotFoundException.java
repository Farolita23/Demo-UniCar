package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1730991753157492056L;

	public TripNotFoundException(Long id) {
		super("No existe el viaje con id: " + id);
	}

}
