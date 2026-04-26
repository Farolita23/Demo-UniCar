package com.daw.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad que intercepta cada petición HTTP para validar el token JWT.
 *
 * Se ejecuta una única vez por solicitud (hereda de {@link OncePerRequestFilter}).
 * Extrae el token del encabezado {@code Authorization: Bearer <token>}, lo valida
 * y, si es correcto, establece la autenticación en el {@link SecurityContextHolder}
 * para que los filtros y controladores posteriores la reconozcan.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see JwtUtil
 * @see SecurityConfig
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Lógica principal del filtro: extrae, valida e inyecta la autenticación JWT.
     *
     * Si la cabecera {@code Authorization} está ausente o no empieza por
     * {@code "Bearer "}, la petición pasa al siguiente filtro sin autenticar.
     * Si el token está malformado, también se delega al siguiente filtro.
     *
     * @param request     petición HTTP entrante
     * @param response    respuesta HTTP saliente
     * @param filterChain cadena de filtros de Spring Security
     * @throws ServletException si se produce un error de servlet
     * @throws IOException      si se produce un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Si no hay cabecera Authorization o no empieza por "Bearer ", dejamos pasar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token malformado: se continúa la cadena sin autenticar
            filterChain.doFilter(request, response);
            return;
        }

        // Solo se autentica si hay username y el contexto aún no tiene autenticación
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
