package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra un reporte con el identificador proporcionado.
 *
 * Mapeada automáticamente a una respuesta HTTP {@code 404 Not Found} por el
 * manejador global de excepciones {@code ExceptionController}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5933229656683237579L;

	/**
	 * Construye la excepción con un mensaje que incluye el identificador del reporte no encontrado.
	 *
	 * @param id identificador del reporte que no existe en el sistema
	 */
	public ReportNotFoundException(Long id) {
		super("No existe el reporte con id: " + id);
	}

}
