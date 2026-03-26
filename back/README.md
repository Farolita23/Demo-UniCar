# UniCar Backend v2 — Spring Boot

## Descripción
Backend REST para UniCar, plataforma de carpooling universitario. Basado en Spring Boot 3 + Spring Security + JWT.

## Tecnologías
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + MySQL
- MapStruct, Lombok
- SpringDoc OpenAPI (Swagger)

## Nuevos endpoints en v2

### Viajes — Gestión de pasajeros
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/trip/{id}/request/{userId}` | Pasajero solicita unirse |
| DELETE | `/api/trip/{id}/request/{userId}` | Pasajero cancela solicitud |
| POST | `/api/trip/{id}/accept/{requesterId}` | Conductor acepta pasajero |
| DELETE | `/api/trip/{id}/reject/{requesterId}` | Conductor rechaza pasajero |
| DELETE | `/api/trip/{id}/leave/{userId}` | Pasajero confirmado abandona |

### Nuevos campos en User
- `profileImageUrl` (String, nullable) — URL de foto de perfil

## Endpoints públicos (sin token)
- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/trip` — Listado de viajes
- `GET /api/trip/{id}` — Detalle de viaje
- `POST /api/trip/search` — Búsqueda de viajes
- `GET /api/campus` — Listado de campus
- `GET /api/town` — Listado de localidades

## Configuración
Edita `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/UniCarDB
spring.datasource.username=root
spring.datasource.password=root
jwt.secret=unaClaveSecretaMuyLargaYSegura1234567890
jwt.expiration=86400000
```

## Ejecución
```bash
mvn spring-boot:run
```

Swagger UI disponible en: `http://localhost:8080/swagger-ui/index.html`
