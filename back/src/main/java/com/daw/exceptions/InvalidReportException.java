package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un reporte no cumple las reglas de negocio establecidas.
 *
 * Un usuario solo puede reportar a otro si han compartido al menos un viaje juntos
 * y no puede reportarse a sí mismo. Mapeada a HTTP {@code 400 Bad Request}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidReportException extends RuntimeException {

	private static final long serialVersionUID = -7597909467370878591L;

	/**
	 * Construye la excepción indicando los identificadores de los usuarios implicados.
	 *
	 * @param idUserReport   identificador del usuario que intenta realizar el reporte
	 * @param idReportedUser identificador del usuario que se intenta reportar
	 */
	public InvalidReportException(Long idUserReport, Long idReportedUser) {
		super("Como usuario con id " + idUserReport + " no puedes reportar al usuario con id " + idReportedUser);
	}

}
