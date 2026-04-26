package com.daw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Configuración central de seguridad de la aplicación UniCar.
 *
 * Define la cadena de filtros de Spring Security con las siguientes políticas:
 * 
 *   Sesión stateless (JWT): no se crea ni gestiona sesión HTTP.
 *   CORS habilitado para el cliente Angular en {@code localhost:4200}.
 *   CSRF desactivado al operar exclusivamente con tokens JWT.
 *   Autorización por rutas: endpoints públicos, autenticados y de administrador.
 * 
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see JwtAuthFilter
 * @see JwtUtil
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Define la cadena de filtros de seguridad HTTP con las reglas de autorización por ruta.
     *
     * @param http objeto de configuración de seguridad HTTP proporcionado por Spring
     * @return cadena de filtros de seguridad construida y lista para ser registrada
     * @throws Exception si ocurre algún error durante la configuración de la cadena
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS debe procesarse antes que cualquier comprobación de autenticación
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Peticiones preflight OPTIONS siempre autorizadas
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Endpoints públicos de autenticación
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/auth/me").authenticated()
                // Endpoints públicos de consulta
                .requestMatchers(HttpMethod.GET,  "/api/trip").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/trip/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/trip/search").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/trip/future").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/campus").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/town").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/user/car-owner/{idCar}").permitAll()
                // Búsqueda de usuarios (requiere autenticación)
                .requestMatchers(HttpMethod.GET, "/api/user/search").authenticated()
                // Perfil público de usuario
                .requestMatchers(HttpMethod.GET, "/api/user/{id}").permitAll()
                // Endpoints exclusivos de administrador
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Documentación Swagger
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura la política CORS permitiendo al cliente Angular interactuar con la API.
     *
     * Se permite cualquier cabecera en la solicitud y se expone {@code Authorization}
     * en la respuesta. El resultado del preflight se cachea durante una hora.
     *
     * @return fuente de configuración CORS registrada para todos los patrones de ruta
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // cachea el preflight 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Crea el proveedor de autenticación DAO que delega en {@link UserDetailsService}
     * y utiliza BCrypt para la verificación de contraseñas.
     *
     * @return proveedor de autenticación configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expone el {@link AuthenticationManager} del contexto de Spring Security como bean.
     *
     * @param config configuración de autenticación de Spring
     * @return gestor de autenticación listo para ser inyectado
     * @throws Exception si no se puede obtener el gestor de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el codificador de contraseñas basado en el algoritmo BCrypt.
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
