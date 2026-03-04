package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidReportException extends RuntimeException {

	private static final long serialVersionUID = -7597909467370878591L;

	public InvalidReportException(Long idUserReport, Long idReportedUser) {
		super("Como usuario con id " + idUserReport + " no puedes reportar al usuario con id " + idReportedUser);
	}

}
