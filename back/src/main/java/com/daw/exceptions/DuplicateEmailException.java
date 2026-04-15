package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

	private static final long serialVersionUID = -3454519544641769175L;

	public DuplicateEmailException() {
		super("Ya existe un usuario con ese nombre");
	}

}
