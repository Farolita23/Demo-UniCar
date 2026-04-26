package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un vehículo con el identificador proporcionado.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 404 Not Found} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1064475690647727844L;

	/**
	 * Construye la excepción con un mensaje que incluye el identificador del vehículo no encontrado.
	 *
	 * @param id identificador del vehículo que no existe en el sistema
	 */
	public CarNotFoundException(Long id) {
		super("No existe el coche con id: " + id);
	}

}
