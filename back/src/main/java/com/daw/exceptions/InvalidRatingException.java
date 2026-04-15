package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRatingException extends RuntimeException {

	private static final long serialVersionUID = -2476532641088884068L;

	public InvalidRatingException(Long idUserRate, Long idRatedUser) {
		super("Como usuario con id " + idUserRate + " no puedes valorar al usuario con id " + idRatedUser);
	}

}
