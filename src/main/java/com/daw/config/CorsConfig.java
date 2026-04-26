package com.daw.config;

/**
 * Clase de configuración CORS reservada para uso futuro.
 *
 * La política CORS activa se gestiona íntegramente desde {@code SecurityConfig},
 * donde se registra el bean {@code CorsConfigurationSource} junto con la cadena
 * de filtros de Spring Security. Mantener la configuración CORS en un único lugar
 * evita conflictos en el manejo de peticiones preflight {@code OPTIONS}.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see com.daw.security.SecurityConfig
 */
public class CorsConfig {
}
