package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un usuario intenta valorar a otro por segunda vez.
 *
 * El sistema permite una única valoración por par de usuarios. Mapeada a
 * HTTP {@code 409 Conflict}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class RatingAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -369678223132121655L;

	/**
	 * Construye la excepción indicando los identificadores de los usuarios implicados.
	 *
	 * @param idUserRate  identificador del usuario que ya emitió una valoración previa
	 * @param idRatedUser identificador del usuario ya valorado
	 */
	public RatingAlreadyExistsException(Long idUserRate, Long idRatedUser) {
		super("Como usuario con id " + idUserRate + " solo puedes hacer una única valoración del usuario con id " + idRatedUser);
	}

}
