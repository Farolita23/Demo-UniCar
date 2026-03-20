package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RatingAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -369678223132121655L;

	public RatingAlreadyExistsException(Long idUserRate, Long idRatedUser) {
		super("Como usuario con id " + idUserRate + " solo puedes hacer una única valoración del usuario con id " + idRatedUser);
	}

}
