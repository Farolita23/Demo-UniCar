package com.daw.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daw.exceptions.CampusNotFoundException;
import com.daw.exceptions.CarNotFoundException;
import com.daw.exceptions.DuplicateEmailException;
import com.daw.exceptions.DuplicateUsernameException;
import com.daw.exceptions.DuplicateZipCodeException;
import com.daw.exceptions.EntityWithDependenciesException;
import com.daw.exceptions.HasPassengersException;
import com.daw.exceptions.InvalidRatingException;
import com.daw.exceptions.InvalidReportException;
import com.daw.exceptions.RatingAlreadyExistsException;
import com.daw.exceptions.RatingNotFoundException;
import com.daw.exceptions.ReportNotFoundException;
import com.daw.exceptions.TownNotFoundException;
import com.daw.exceptions.TripActionException;
import com.daw.exceptions.TripNotFoundException;
import com.daw.exceptions.UserNotFoundException;

/**
 * Manejador global de excepciones
 *
 * Intercepta todas las excepciones de dominio y de seguridad lanzadas por los servicios
 * y las traduce a respuestas HTTP estandarizadas con el siguiente esquema JSON:
 * 
 * {
 *   "timestamp": "...",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "No existe el campus con id: 5"
 * }
 * 
 *
 * @author Javier Falcon
 * @version 1.0.0
 */
@RestControllerAdvice
public class ExceptionController {

    /**
     * Construye el cuerpo de error estándar para cualquier respuesta de fallo.
     *
     * @param status  código de estado HTTP a devolver
     * @param message mensaje descriptivo del error
     * @return {@link ResponseEntity} con el cuerpo de error estandarizado
     */
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    // ─── 404 Not Found ────────────────────────────────────────────────────────

    /** @see UserNotFoundException */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see CampusNotFoundException */
    @ExceptionHandler(CampusNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCampusNotFound(CampusNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see CarNotFoundException */
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCarNotFound(CarNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see TownNotFoundException */
    @ExceptionHandler(TownNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTownNotFound(TownNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see TripNotFoundException */
    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTripNotFound(TripNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see RatingNotFoundException */
    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRatingNotFound(RatingNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** @see ReportNotFoundException */
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReportNotFound(ReportNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ─── 409 Conflict ─────────────────────────────────────────────────────────

    /** @see DuplicateUsernameException */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUsername(DuplicateUsernameException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see DuplicateEmailException */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see DuplicateZipCodeException */
    @ExceptionHandler(DuplicateZipCodeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateZipCode(DuplicateZipCodeException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see EntityWithDependenciesException */
    @ExceptionHandler(EntityWithDependenciesException.class)
    public ResponseEntity<Map<String, Object>> handleEntityWithDependencies(EntityWithDependenciesException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see HasPassengersException */
    @ExceptionHandler(HasPassengersException.class)
    public ResponseEntity<Map<String, Object>> handleHasPassengers(HasPassengersException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see RatingAlreadyExistsException */
    @ExceptionHandler(RatingAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRatingAlreadyExists(RatingAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** @see TripActionException */
    @ExceptionHandler(TripActionException.class)
    public ResponseEntity<Map<String, Object>> handleTripAction(TripActionException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ─── 400 Bad Request ──────────────────────────────────────────────────────

    /** @see InvalidRatingException */
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRating(InvalidRatingException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** @see InvalidReportException */
    @ExceptionHandler(InvalidReportException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidReport(InvalidReportException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maneja los errores de validación de {@code @Valid} / {@code @Validated} en los DTOs.
     *
     * Devuelve un mapa con los campos inválidos y sus mensajes de error correspondientes.
     *
     * @param ex excepción de validación con el detalle de los campos fallidos
     * @return {@code 400 Bad Request} con el mapa {@code campos → mensajes de error}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        body.put("fields", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // ─── 401 Unauthorized (Spring Security) ──────────────────────────────────

    /** @see BadCredentialsException */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
    }

    /** @see UsernameNotFoundException */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
    }

    // ─── 500 Fallback ─────────────────────────────────────────────────────────

    /**
     * Manejador de último recurso para excepciones no contempladas específicamente.
     *
     * @param ex excepción no capturada por los manejadores anteriores
     * @return {@code 500 Internal Server Error} con el mensaje de la excepción
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + ex.getMessage());
    }
}
