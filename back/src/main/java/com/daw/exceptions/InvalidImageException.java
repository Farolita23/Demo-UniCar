package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el archivo subido como foto no es una imagen válida o supera los límites.
 *
 * Mapeada a {@code 400 Bad Request} por {@code ExceptionController}.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidImageException extends RuntimeException {

	private static final long serialVersionUID = 4172907365813522903L;

	public InvalidImageException(String message) {
		super(message);
	}

}
