package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateUsernameException extends RuntimeException {

	private static final long serialVersionUID = -1847719418757507977L;
	
	public DuplicateUsernameException() {
		super("Ya existe un usuario con ese nombre");
	}

}
