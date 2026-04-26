package com.daw.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI (Swagger) para la plataforma UniCar.
 *
 * Define los metadatos de la API expuesta en {@code /swagger-ui/index.html},
 * incluyendo el esquema de seguridad Bearer JWT que permite autenticar las
 * peticiones directamente desde la interfaz de Swagger UI.
 *
 * @author Moises Junior
 * @version 1.0.0
 * @see SecurityConfig
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "UniCar API",
        version = "2.0",
        description = "API REST para la plataforma de carpooling universitario UniCar"
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {}
