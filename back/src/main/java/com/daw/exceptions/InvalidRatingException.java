package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando una valoración no cumple las reglas de negocio establecidas.
 *
 * Un usuario solo puede valorar a otro si han compartido al menos un viaje juntos
 * y no puede valorarse a sí mismo. Mapeada a HTTP {@code 400 Bad Request}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRatingException extends RuntimeException {

	private static final long serialVersionUID = -2476532641088884068L;

	/**
	 * Construye la excepción indicando los identificadores de los usuarios implicados.
	 *
	 * @param idUserRate   identificador del usuario que intenta realizar la valoración
	 * @param idRatedUser  identificador del usuario que se intenta valorar
	 */
	public InvalidRatingException(Long idUserRate, Long idRatedUser) {
		super("Como usuario con id " + idUserRate + " no puedes valorar al usuario con id " + idRatedUser);
	}

}
