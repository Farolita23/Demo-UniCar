package com.daw.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando el conductor intenta cancelar un viaje que ya tiene pasajeros confirmados.
 *
 * Protege a los pasajeros aceptados frente a cancelaciones unilaterales del conductor.
 * Para poder eliminar el viaje, el conductor debe expulsar primero a todos los pasajeros.
 * Mapeada a HTTP {@code 409 Conflict}.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class HasPassengersException extends RuntimeException {

	private static final long serialVersionUID = 1956882336198224061L;

	/**
	 * Construye la excepción indicando el identificador del viaje con pasajeros activos.
	 *
	 * @param id identificador del viaje que no puede ser eliminado
	 */
	public HasPassengersException(Long id) {
		super("Para cancelar este viaje con id " + id + ", no puedes tener ningún pasajero");
	}

}
