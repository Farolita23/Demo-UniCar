package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateZipCodeException extends RuntimeException {

	private static final long serialVersionUID = -750627325956230803L;

	public DuplicateZipCodeException() {
		super("Ya existe ese código postal, asociado a un pueblo diferente");
	}

}
