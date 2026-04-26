package com.daw.exceptions;

/**
 * Excepción lanzada cuando una acción sobre un viaje viola las reglas de negocio definidas.
 *
 * Agrupa situaciones como: solicitar plaza en un viaje propio, intentar unirse
 * a un viaje sin plazas libres, rechazar a un usuario que no tiene solicitud
 * pendiente, o abandonar un viaje en el que no se participa. Mapeada a
 * HTTP {@code 409 Conflict} por el manejador global de excepciones.
 *
 * @author Javier Falcon
 * @version 1.0.0
 * @see com.daw.controller.ExceptionController
 */
public class TripActionException extends RuntimeException {

    /**
     * Construye la excepción con el mensaje descriptivo de la infracción cometida.
     *
     * @param message descripción detallada del motivo por el que la acción no está permitida
     */
    public TripActionException(String message) {
        super(message);
    }
}
