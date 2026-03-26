package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TownNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5667216281346935158L;

	public TownNotFoundException(Long id) {
		super("No existe el pueblo con id: " + id);
	}

}
