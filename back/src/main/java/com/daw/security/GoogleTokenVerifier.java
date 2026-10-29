package com.daw.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Verifica ID tokens de Google Identity Services contra el endpoint {@code tokeninfo} de Google,
 * que valida la firma y la caducidad. Aquí se comprueba además la audiencia (nuestro Client ID),
 * el emisor y que el correo esté verificado.
 */
@Component
public class GoogleTokenVerifier {

    /** Datos del usuario de Google extraídos de un token válido. */
    public record GoogleUser(String email, String name, String picture) {}

    private static final String TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";

    private final RestClient restClient = RestClient.create();

    @Value("${google.client-id}")
    private String clientId;

    /**
     * @param idToken ID token recibido del cliente
     * @return datos del usuario de Google
     * @throws BadCredentialsException (401) si el token no es válido para esta aplicación
     */
    @SuppressWarnings("unchecked")
    public GoogleUser verify(String idToken) {
        Map<String, Object> claims;
        try {
            claims = restClient.get().uri(TOKENINFO_URL, idToken).retrieve().body(Map.class);
        } catch (Exception e) {
            throw unauthorized();
        }
        if (claims == null
                || !clientId.equals(claims.get("aud"))
                || !isGoogleIssuer(String.valueOf(claims.get("iss")))
                || !"true".equals(String.valueOf(claims.get("email_verified")))
                || claims.get("email") == null) {
            throw unauthorized();
        }
        String email = String.valueOf(claims.get("email"));
        String name = claims.get("name") != null ? String.valueOf(claims.get("name")) : email;
        String picture = claims.get("picture") != null ? String.valueOf(claims.get("picture")) : null;
        return new GoogleUser(email, name, picture);
    }

    private boolean isGoogleIssuer(String iss) {
        return "accounts.google.com".equals(iss) || "https://accounts.google.com".equals(iss);
    }

    private BadCredentialsException unauthorized() {
        return new BadCredentialsException("Invalid Google token");
    }
}
