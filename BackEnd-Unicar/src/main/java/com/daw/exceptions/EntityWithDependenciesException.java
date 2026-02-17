package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EntityWithDependenciesException extends RuntimeException {

	private static final long serialVersionUID = -880244266181412575L;

	public EntityWithDependenciesException(String entity, Long id) {
		super("No se puede eliminar la instancia de la entidad " + entity + " con id " + id + ", debido a que está asociado a otros elementos indispensables.");
	}

}
