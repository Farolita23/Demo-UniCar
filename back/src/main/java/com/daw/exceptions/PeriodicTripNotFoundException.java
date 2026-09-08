package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un viaje periódico con el identificador proporcionado.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 404 Not Found} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PeriodicTripNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8421559943178812901L;

	/**
	 * Construye la excepción con un mensaje que incluye el identificador del viaje periódico no encontrado.
	 *
	 * @param id identificador del viaje periódico que no existe en el sistema
	 */
	public PeriodicTripNotFoundException(Long id) {
		super("No existe el viaje periódico con id: " + id);
	}

}
