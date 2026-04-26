package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando se intenta eliminar una entidad que tiene dependencias activas.
 *
 * Se utiliza para proteger la integridad referencial a nivel de servicio antes de
 * delegar al repositorio. Por ejemplo, impide borrar un campus que tenga viajes o
 * usuarios asociados. Mapeada a HTTP {@code 409 Conflict}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class EntityWithDependenciesException extends RuntimeException {

	private static final long serialVersionUID = -880244266181412575L;

	/**
	 * Construye la excepción indicando el tipo de entidad y su identificador.
	 *
	 * @param entity nombre de la entidad que no puede ser eliminada
	 * @param id     identificador de la instancia con dependencias
	 */
	public EntityWithDependenciesException(String entity, Long id) {
		super("No se puede eliminar la instancia de la entidad " + entity + " con id " + id + ", debido a que está asociado a otros elementos indispensables.");
	}

}
