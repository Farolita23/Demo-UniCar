package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta registrar un correo electrónico ya existente en el sistema.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 409 Conflict} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

	private static final long serialVersionUID = -3454519544641769175L;

	/**
	 * Construye la excepción con un mensaje informativo sobre el conflicto de correo electrónico.
	 */
	public DuplicateEmailException() {
		super("Ya existe un usuario con ese nombre");
	}

}
