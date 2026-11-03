# Indices y consultas: notas de rendimiento

BD de medida: 20.000 usuarios, 40.000 viajes (32.001 futuros), 60.000 valoraciones, MySQL 8.0.
Tiempos de `EXPLAIN ANALYZE` (ms de ejecucion en MySQL), cada indice medido de forma aislada.

## Indices conservados

| Indice | Consulta a la que sirve | Antes | Despues |
|---|---|---|---|
| `trip(departure_date, departure_time)` `idx_trip_departure` | `findFutureTrips`, `findSuggestedTrips` y `TripSpecification` (busqueda): `departure_date >= CURRENT_DATE ORDER BY departure_date, departure_time LIMIT n` | table scan de 40.000 filas + sort: 25-39 ms | index range scan que corta en el LIMIT: 0,2-0,4 ms (unas 100 veces menos) |
| `rating(rated_user_id, rating)` `idx_rating_rated_user_rating` | `statsForRatedUsers`: `AVG(rating), COUNT(*) ... GROUP BY rated_user_id` | pagina de 20 usuarios: 0,29 ms; todos los usuarios: 82-101 ms | 0,07 ms; todos: 15 ms (cubriente, sin tocar la tabla) |

Coste: 1,5 MB cada uno (con 40.000 / 60.000 filas) y una entrada mas por INSERT. El de rating mejora poco en
valor absoluto con paginas pequenas; se mantiene por ser barato y por escalar mejor en agregaciones amplias.
Arranque con `ddl-auto=update` sobre la BD completa: ambos se crean sin errores y el backend arranca en ~16 s.

## Indices evaluados y descartados

- `trip(campus_id, departure_date, departure_time)` y `trip(town_id, departure_date, departure_time)`: con
  `idx_trip_departure` la consulta por campus ya baja a 0,25 ms (un campus es el 12% de las filas, se encuentran
  20 resultados casi al instante); por localidad pasa de 0,64 a 0,06 ms, ganancia absoluta insignificante que no
  justifica 2 indices mas. Campus+localidad (~50 filas) ya usa las FK (1,5 ms).
- `trip(is_to_campus)`: dos valores, sin selectividad.
- `trip_passenger(passenger_id)`, `trip_requester(requester_id)`, `car(user_id)`, `favorite(user_id, ...)`,
  `report(reported_user_id / user_report_id)`, `periodic_trip(car_id)`, `trip(car_id / periodic_trip_id)`: ya los
  cubren las FK, la PK compuesta o la UNIQUE existentes (EXPLAIN muestra "covering index lookup", < 0,1 ms).
  `existsSharedTrip`, `findRecommendedTrips`, `findTripsAsADriver/AsAPassenger` y el filtro de plazas libres
  usan solo indices existentes (0,05-2 ms por ejecucion). Su coste real es el patron N+1, no el SQL.
- `user(name)` para busqueda por prefijo: 0,3 ms frente a 1,1 ms, pero cambia la semantica (empieza por, en vez
  de contiene) y para el count del `Page` sigue habiendo un OR de dos rangos. Descartado.

## Busqueda de usuarios (`UserRepository.searchByNameOrUsername`, archivo de A2): parche propuesto

El cuello esta en el `count(*)` de la paginacion: con `LOWER(..) LIKE '%q%'` siempre escanea los 20.000
usuarios (10-15 ms, lineal con el numero de usuarios); la consulta de la pagina solo es lenta cuando hay pocas
coincidencias (25 ms en una busqueda sin resultados). Quitar `LOWER` no ayuda (12 ms): la columna ya es
`utf8mb4_unicode_ci`, que no distingue mayusculas.

Medidas (EXPLAIN ANALYZE):

| Variante | count(*) 'perfuser123' | pagina sin resultados | pagina con muchos resultados |
|---|---|---|---|
| actual `LOWER LIKE '%q%'` | 14,8 ms | 24,6 ms | 1,4 ms |
| `LIKE '%q%'` sin LOWER (mismo resultado) | 12,1 ms | 20,8 ms | 1,1 ms |
| FULLTEXT ngram + `MATCH ... AGAINST` (modo booleano, frase) | 0,0001 ms | 0,005 ms | 3,9-6,9 ms |
| prefijo `LIKE 'q%'` con `idx` sobre name | 0,5 ms | - | 0,3 ms |

El FULLTEXT con `WITH PARSER ngram` (token de 2 caracteres) devuelve exactamente los mismos ids que el
`contains` actual para consultas de 2 o mas caracteres (comprobado con 'perfuser1', 'usuario', 'prueba 12',
'ser12', 'PERF', '99', 'zz'). Con 1 caracter no devuelve nada, asi que hay que conservar el `LIKE` para q de
longitud < 2. Para que sea uniforme en todos los entornos el indice se crea con el script (`indices.sql`,
bloque opcional), no con Hibernate.

Parche propuesto para `UserRepository` (requiere el indice `ft_user_name_username`):

```java
@Query(value = "SELECT * FROM user u WHERE MATCH(u.name, u.username) AGAINST (:phrase IN BOOLEAN MODE)",
       countQuery = "SELECT COUNT(*) FROM user u WHERE MATCH(u.name, u.username) AGAINST (:phrase IN BOOLEAN MODE)",
       nativeQuery = true)
Page<User> searchByNameOrUsernameFulltext(@Param("phrase") String phrase, Pageable pageable);
```

El servicio llama a esta variante con `phrase = "\"" + q.replace("\"", "") + "\""` cuando `q.trim().length() >= 2`
y a la actual cuando es menor. Ojo con el `Pageable`: en consulta nativa la ordenacion debe usar nombres de
columna SQL, no de propiedad.
