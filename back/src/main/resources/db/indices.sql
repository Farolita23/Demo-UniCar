-- Indices de rendimiento de UniCar (MySQL 8).
-- Referencia: las entidades Trip y Rating ya los declaran con @Table(indexes=...) y
-- spring.jpa.hibernate.ddl-auto=update los crea al arrancar. Este script sirve para
-- aplicarlos a mano (por ejemplo en produccion con ddl-auto=none). Es idempotente:
-- MySQL 8 no tiene CREATE INDEX IF NOT EXISTS, asi que se comprueba information_schema.

DELIMITER //

DROP PROCEDURE IF EXISTS unicar_crear_indice//
CREATE PROCEDURE unicar_crear_indice(IN p_tabla VARCHAR(64), IN p_indice VARCHAR(64), IN p_ddl TEXT)
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.statistics
                 WHERE table_schema = DATABASE() AND table_name = p_tabla AND index_name = p_indice) THEN
    SET @ddl = p_ddl;
    PREPARE s FROM @ddl; EXECUTE s; DEALLOCATE PREPARE s;
  END IF;
END//

DELIMITER ;

-- Listados de viajes futuros: WHERE departure_date >= CURRENT_DATE ORDER BY departure_date, departure_time LIMIT n
-- (future, suggested, search por fecha/hora). Evita escanear y ordenar toda la tabla trip.
CALL unicar_crear_indice('trip', 'idx_trip_departure',
  'CREATE INDEX idx_trip_departure ON trip (departure_date, departure_time)');

-- Estadisticas de valoracion por usuario: SELECT rated_user_id, AVG(rating), COUNT(*) ... GROUP BY rated_user_id.
-- Indice cubriente: no hace falta leer la tabla. Hace redundante fk_rating_rated_user (ver nota al final).
CALL unicar_crear_indice('rating', 'idx_rating_rated_user_rating',
  'CREATE INDEX idx_rating_rated_user_rating ON rating (rated_user_id, rating)');

-- OPCIONAL (solo si se aplica el parche de busqueda de usuarios descrito en README-rendimiento.md).
-- Hibernate no puede declarar FULLTEXT con parser ngram, por eso solo existe aqui.
-- CALL unicar_crear_indice('user', 'ft_user_name_username',
--   'CREATE FULLTEXT INDEX ft_user_name_username ON user (name, username) WITH PARSER ngram');

DROP PROCEDURE unicar_crear_indice;

-- Nota: en una BD que ya tenia fk_rating_rated_user, el nuevo indice lo cubre por completo y se puede
-- eliminar para ahorrar escrituras/espacio: ALTER TABLE rating DROP INDEX fk_rating_rated_user;
-- (la FK sigue funcionando, MySQL usa el indice compuesto). No se hace automaticamente.
