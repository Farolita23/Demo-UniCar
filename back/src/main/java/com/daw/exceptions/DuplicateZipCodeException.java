package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta crear una localidad con un código postal ya registrado.
 *
 * Garantiza la unicidad del campo {@code zip_code} en la tabla {@code town}.
 * Mapeada automáticamente a una respuesta HTTP {@code 409 Conflict} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateZipCodeException extends RuntimeException {

	private static final long serialVersionUID = -750627325956230803L;

	/**
	 * Construye la excepción con un mensaje informativo sobre el conflicto de código postal.
	 */
	public DuplicateZipCodeException() {
		super("Ya existe ese código postal, asociado a un pueblo diferente");
	}

}
