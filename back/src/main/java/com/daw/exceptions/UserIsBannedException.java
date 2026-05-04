package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un usuario está baneado.
 *
 *  Mapeada automáticamente a una respuesta HTTP {@code 403 Forbidden} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserIsBannedException extends RuntimeException {

	private static final long serialVersionUID = 6336995510841711170L;

	/**
	 * Constructs a <code>UserIsBannedException</code> with the specified message.
	 * @param msg the detail message.
	 */
	public UserIsBannedException(String msg) {
		super(msg);
	}

}
