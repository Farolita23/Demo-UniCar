package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un usuario con el identificador proporcionado.
 *
 *  Mapeada automáticamente a una respuesta HTTP {@code 404 Not Found} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6336995510841711170L;

	/**
	 * Construye la excepción con un mensaje que incluye el identificador del usuario no encontrado.
	 *
	 * @param id identificador del usuario que no existe en el sistema
	 */
	public UserNotFoundException(Long id) {
		super("No existe el usuario con id: " + id);
	}

}
