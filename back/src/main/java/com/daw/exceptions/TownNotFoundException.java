package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra una localidad con el identificador proporcionado.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 404 Not Found} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TownNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5667216281346935158L;

	/**
	 * Construye la excepción con un mensaje que incluye el identificador de la localidad no encontrada.
	 *
	 * @param id identificador de la localidad que no existe en el sistema
	 */
	public TownNotFoundException(Long id) {
		super("No existe el pueblo con id: " + id);
	}

}
