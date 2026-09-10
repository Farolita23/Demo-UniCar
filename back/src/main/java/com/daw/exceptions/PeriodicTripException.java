package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando la definición de un viaje periódico viola las reglas de negocio.
 *
 * Agrupa situaciones como: fecha de fin anterior a la de inicio, rango de fechas
 * demasiado amplio, o una combinación de días de la semana y fechas que no genera
 * ningún viaje. Mapeada a HTTP {@code 400 Bad Request} por el manejador global de
 * excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PeriodicTripException extends RuntimeException {

	private static final long serialVersionUID = 5541229943178812902L;

	/**
	 * Construye la excepción con el mensaje descriptivo de la infracción cometida.
	 *
	 * @param message descripción detallada del motivo por el que la definición no es válida
	 */
	public PeriodicTripException(String message) {
		super(message);
	}
}
