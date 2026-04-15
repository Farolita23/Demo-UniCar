package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5933229656683237579L;

	public ReportNotFoundException(Long id) {
		super("No existe el reporte con id: " + id);
	}

}
