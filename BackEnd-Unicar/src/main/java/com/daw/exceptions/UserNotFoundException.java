package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6336995510841711170L;
	
	public UserNotFoundException(Long id) {
		super("No existe el usuario con id: " + id);
	}

}
