package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class HasPassengersException extends RuntimeException {

	private static final long serialVersionUID = 1956882336198224061L;

	public HasPassengersException(Long id) {
		super("Para cancelar este viaje con id " + id + ", no puedes tener ningún pasajero");
	}

}
