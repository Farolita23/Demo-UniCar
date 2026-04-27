package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta registrar un nombre de usuario ya existente en el sistema.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 409 Conflict} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateUsernameException extends RuntimeException {

	private static final long serialVersionUID = -1847719418757507977L;

	/**
	 * Construye la excepción con un mensaje informativo sobre el conflicto de nombre de usuario.
	 */
	public DuplicateUsernameException() {
		super("Ya existe un usuario con ese nombre");
	}

}
