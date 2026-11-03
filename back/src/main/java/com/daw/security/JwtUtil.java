package com.daw.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Componente utilitario para la generación y validación de tokens JWT.
 *
 * Encapsula toda la lógica criptográfica asociada a JSON Web Tokens,
 * incluyendo la firma con HMAC-SHA256, la extracción de claims y la
 * comprobación de caducidad. El secreto y el tiempo de expiración se
 * inyectan desde las propiedades de la aplicación.
 *
 * @author Adam Gavira
 * @version 1.0.0
 * @see JwtAuthFilter
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /** Longitud minima del secreto para HS256 (256 bits = 32 bytes). */
    private static final int MIN_SECRET_BYTES = 32;

    @Value("${jwt.secret:}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /** Clave de firma HMAC, construida una sola vez al arrancar. */
    private Key signingKey;

    /**
     * Resuelve la clave de firma al iniciar la aplicacion. Si no se ha configurado un secreto
     * (propiedad {@code jwt.secret} / variable {@code JWT_SECRET}), se genera uno aleatorio seguro
     * para este arranque: la aplicacion sigue funcionando, pero los tokens dejan de ser validos al
     * reiniciar. En produccion debe definirse {@code JWT_SECRET} para que los tokens sean estables.
     */
    @PostConstruct
    void initSigningKey() {
        if (secret == null || secret.isBlank()) {
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            log.warn("No se ha configurado JWT_SECRET: se usa una clave aleatoria temporal. "
                    + "Define JWT_SECRET (>= {} bytes) para que los tokens sobrevivan a un reinicio.",
                    MIN_SECRET_BYTES);
            return;
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "JWT_SECRET demasiado corto: se requieren al menos " + MIN_SECRET_BYTES + " bytes para HS256");
        }
        signingKey = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Devuelve la clave de firma HMAC resuelta al arrancar.
     *
     * @return clave criptográfica lista para firmar o verificar tokens
     */
    private Key getSigningKey() {
        return signingKey;
    }

    /**
     * Genera un token JWT que contiene únicamente el nombre de usuario como sujeto.
     *
     * @param username nombre de usuario que se almacena en el claim {@code sub}
     * @return token JWT compacto y firmado
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera un token JWT que incluye el nombre de usuario y el rol del usuario.
     *
     * @param username nombre de usuario almacenado en el claim {@code sub}
     * @param role     rol del usuario almacenado como claim personalizado {@code role}
     * @return token JWT compacto y firmado
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario del claim {@code sub} del token.
     *
     * @param token token JWT del que se extrae el sujeto
     * @return nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el rol del usuario del claim personalizado {@code role}.
     *
     * @param token token JWT del que se extrae el rol
     * @return rol del usuario o {@code null} si el claim no está presente
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extrae la fecha de expiración del token.
     *
     * @param token token JWT a inspeccionar
     * @return fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim genérico del token aplicando la función resolutora proporcionada.
     *
     * @param <T>            tipo del valor del claim a extraer
     * @param token          token JWT a parsear
     * @param claimsResolver función que mapea el objeto {@link Claims} al valor deseado
     * @return valor del claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsea y verifica la firma del token, devolviendo todos sus claims.
     *
     * @param token token JWT a parsear
     * @return objeto {@link Claims} con todos los datos del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Comprueba si el token ha superado su fecha de expiración.
     *
     * @param token token JWT a verificar
     * @return {@code true} si el token está caducado, {@code false} en caso contrario
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Valida que el token pertenece al usuario indicado y no ha expirado.
     *
     * @param token    token JWT a validar
     * @param username nombre de usuario contra el que se compara el claim {@code sub}
     * @return {@code true} si el token es válido y corresponde al usuario, {@code false} en caso contrario
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}
