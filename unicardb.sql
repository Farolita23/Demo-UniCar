-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-04-2026 a las 19:34:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `unicardb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `campus`
--

CREATE TABLE `campus` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `campus`
--

INSERT INTO `campus` (`id`, `address`, `name`) VALUES
(1, 'Av. Reina Mercedes, s/n, 41012 Sevilla', 'US - Reina Mercedes (Ciencias / Informática / Ingeniería)'),
(2, 'C/ Virgen de África, 7, 41011 Sevilla', 'US - Escuela Politécnica Superior (Ingeniería)'),
(3, 'Av. Reina Mercedes, 2, 41012 Sevilla', 'US - ETSAS - Arquitectura'),
(4, 'C/ Enramadilla, 23, 41018 Sevilla', 'US - Ramón y Cajal (Derecho / Económicas / ETSI)'),
(5, 'Av. Doctor Fedriani, s/n, 41009 Sevilla', 'US - Macarena (Medicina / Enfermería)'),
(6, 'C/ Laraña, 3, 41003 Sevilla', 'US - Centro (Bellas Artes / Humanidades)'),
(7, 'Av. Américo Vespucio, 41092 Sevilla', 'US - Cartuja (EPS Osuna / extensiones)'),
(8, 'Ctra. de Utrera, km 1, 41013 Sevilla', 'UPO - Campus Principal'),
(9, 'C/ Escritor Castilla Aguayo, 4, 41005 Sevilla', 'Loyola - Campus Sevilla'),
(10, 'Av. de la Buhaira, 27, 41018 Sevilla', 'ESIC Sevilla'),
(11, 'C/ Antonia Díaz, 1, 41001 Sevilla', 'San Isidoro (adscrito UPO)'),
(12, 'C/ Leonardo da Vinci, 12, 41092 Sevilla', 'UNIA - Sede Sevilla (Monasterio La Cartuja)'),
(13, 'C/ Sevilla, s/n, 41640 Osuna', 'CU Osuna (adscrito US)');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `car`
--

CREATE TABLE `car` (
  `id` bigint(20) NOT NULL,
  `capacity` int(11) NOT NULL,
  `color` varchar(255) NOT NULL,
  `license_plate` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `car`
--

INSERT INTO `car` (`id`, `capacity`, `color`, `license_plate`, `model`, `user_id`) VALUES
(1, 4, 'Gris', '1234 ABC', 'Seat León', 1),
(2, 4, 'Blanco', '5678 DEF', 'Volkswagen Polo', 2),
(3, 5, 'Negro', '9012 GHI', 'Renault Megane', 3),
(4, 4, 'Rojo', '3456 JKL', 'Toyota Yaris', 4),
(5, 4, 'Azul', '7890 MNO', 'Ford Focus', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rating`
--

CREATE TABLE `rating` (
  `id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `rated_user_id` bigint(20) NOT NULL,
  `user_rate_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `report`
--

CREATE TABLE `report` (
  `id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `reason` varchar(255) NOT NULL,
  `reported_user_id` bigint(20) NOT NULL,
  `user_report_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `town`
--

CREATE TABLE `town` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `zip_code` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `town`
--

INSERT INTO `town` (`id`, `name`, `zip_code`) VALUES
(1, 'Sevilla - Centro / Catedral', '41001'),
(2, 'Sevilla - Macarena', '41002'),
(3, 'Sevilla - San Vicente / Triana Norte', '41003'),
(4, 'Sevilla - Santa Cruz / Feria', '41004'),
(5, 'Sevilla - Nervión', '41005'),
(6, 'Sevilla - Cerro-Amate', '41006'),
(7, 'Sevilla - San Pablo / Santa Justa', '41007'),
(8, 'Sevilla - Pino Montano', '41008'),
(9, 'Sevilla - Parque Alcosa / Sevilla Este', '41009'),
(10, 'Sevilla - Triana', '41010'),
(11, 'Sevilla - Los Remedios', '41011'),
(12, 'Sevilla - Bellavista / La Palmera / Reina Mercedes', '41012'),
(13, 'Sevilla - Tabladilla / La Plata', '41013'),
(14, 'Sevilla - Bellavista Sur', '41014'),
(15, 'Sevilla - Valdezorras', '41015'),
(16, 'Sevilla - Torreblanca / Alcosa', '41016'),
(17, 'Sevilla - La Macarena Norte', '41017'),
(18, 'Sevilla - San Jerónimo / Nervión Norte', '41018'),
(19, 'Sevilla - Palmete / Villegas', '41019'),
(20, 'Sevilla - La Barzola / Norte', '41020'),
(21, 'Almensilla', '41110'),
(22, 'Gelves', '41120'),
(23, 'Sanlúcar la Mayor', '41800'),
(24, 'Olivares', '41804'),
(25, 'Benacazón', '41805'),
(26, 'Umbrete', '41806'),
(27, 'Espartinas', '41807'),
(28, 'Villanueva del Ariscal', '41808'),
(29, 'Albaida del Aljarafe', '41809'),
(30, 'Bollullos de la Mitación', '41816'),
(31, 'Huévar del Aljarafe', '41817'),
(32, 'Carrión de los Céspedes', '41820'),
(33, 'Castilleja del Campo', '41821'),
(34, 'Pilas', '41840'),
(35, 'Aznalcázar', '41849'),
(36, 'Camas', '41900'),
(37, 'Valencina de la Concepción', '41907'),
(38, 'Castilleja de Guzmán', '41908'),
(39, 'Salteras', '41909'),
(40, 'San Juan de Aznalfarache', '41920'),
(41, 'Mairena del Aljarafe', '41927'),
(42, 'Palomares del Río', '41928'),
(43, 'Bormujos', '41930'),
(44, 'Tomares', '41940'),
(45, 'Castilleja de la Cuesta', '41950'),
(46, 'Gines', '41960'),
(47, 'Santiponce', '41970'),
(48, 'Coria del Río', '41100'),
(49, 'La Puebla del Río', '41130'),
(50, 'Isla Mayor', '41140'),
(51, 'Villamanrique de la Condesa', '41850'),
(52, 'La Algaba', '41980'),
(53, 'Guillena', '41210'),
(54, 'El Garrobo', '41220'),
(55, 'Castilblanco de los Arroyos', '41230'),
(56, 'Almadén de la Plata', '41240'),
(57, 'Alcolea del Río', '41250'),
(58, 'Peñaflor', '41260'),
(59, 'Tocina', '41270'),
(60, 'La Campana', '41280'),
(61, 'Burguillos', '41290'),
(62, 'La Rinconada', '41300'),
(63, 'Brenes', '41310'),
(64, 'Cantillana', '41320'),
(65, 'Villaverde del Río', '41329'),
(66, 'La Puebla de los Infantes', '41330'),
(67, 'Villanueva del Río y Minas', '41350'),
(68, 'Lora del Río', '41440'),
(69, 'Gerena', '41860'),
(70, 'Aznalcóllar', '41870'),
(71, 'Alcalá del Río', '41880'),
(72, 'El Castillo de las Guardas', '41890'),
(73, 'El Pedroso', '41360'),
(74, 'Cazalla de la Sierra', '41370'),
(75, 'Alanís', '41380'),
(76, 'San Nicolás del Puerto', '41388'),
(77, 'El Real de la Jara', '41389'),
(78, 'Guadalcanal', '41390'),
(79, 'Constantina', '41450'),
(80, 'Las Navas de la Concepción', '41460'),
(81, 'El Ronquillo', '41851'),
(82, 'Écija', '41400'),
(83, 'Carmona', '41410'),
(84, 'Fuentes de Andalucía', '41420'),
(85, 'La Lantejuela', '41430'),
(86, 'La Luisiana', '41438'),
(87, 'Cañada Rosal', '41461'),
(88, 'Alcalá de Guadaíra', '41500'),
(89, 'Mairena del Alcor', '41510'),
(90, 'El Viso del Alcor', '41520'),
(91, 'Arahal', '41600'),
(92, 'Marchena', '41620'),
(93, 'Paradas', '41630'),
(94, 'Osuna', '41640'),
(95, 'La Roda de Andalucía', '41650'),
(96, 'Martín de la Jara', '41659'),
(97, 'Algámitas', '41660'),
(98, 'Villanueva de San Juan', '41661'),
(99, 'El Saucejo', '41665'),
(100, 'Morón de la Frontera', '41530'),
(101, 'Casariche', '41540'),
(102, 'Estepa', '41560'),
(103, 'Lora de Estepa', '41562'),
(104, 'Herrera', '41563'),
(105, 'Marinaleda', '41564'),
(106, 'Gilena', '41565'),
(107, 'Pedrera', '41566'),
(108, 'El Rubio', '41568'),
(109, 'Badolatosa', '41569'),
(110, 'La Puebla de Cazalla', '41570'),
(111, 'Aguadulce', '41590'),
(112, 'Coripe', '41690'),
(113, 'Dos Hermanas', '41700'),
(114, 'Utrera', '41710'),
(115, 'Los Palacios y Villafranca', '41720'),
(116, 'Las Cabezas de San Juan', '41730'),
(117, 'El Coronil', '41731'),
(118, 'Lebrija', '41740'),
(119, 'Los Corrales', '41747'),
(120, 'El Cuervo de Sevilla', '41749'),
(121, 'Los Molares', '41750'),
(122, 'Pruna', '41760'),
(123, 'Montellano', '41770'),
(124, 'Dos Hermanas - Montequinto', '41089'),
(125, 'Écija - Isla Redonda / Las Colonias', '41401'),
(126, 'Carmona - El Viar / Guadajoz', '41411'),
(127, 'Lora del Río - Setefilla', '41441'),
(128, 'Alcalá de Guadaíra - Gandul / Marchanilla', '41501'),
(129, 'Osuna - El Sillero', '41641'),
(130, 'Utrera - Guadalema de los Quintero', '41712'),
(131, 'Los Palacios - Maribáñez', '41721'),
(132, 'Lebrija - Las Cabezuelas', '41741');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trip`
--

CREATE TABLE `trip` (
  `id` bigint(20) NOT NULL,
  `departure_address` varchar(255) NOT NULL,
  `departure_date` date NOT NULL,
  `departure_time` time(6) NOT NULL,
  `is_to_campus` bit(1) NOT NULL,
  `price` decimal(4,2) NOT NULL,
  `campus_id` bigint(20) NOT NULL,
  `car_id` bigint(20) NOT NULL,
  `town_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `trip`
--

INSERT INTO `trip` (`id`, `departure_address`, `departure_date`, `departure_time`, `is_to_campus`, `price`, `campus_id`, `car_id`, `town_id`) VALUES
(1, 'Av. de la Libertad, 12, Dos Hermanas', '2026-04-06', '08:00:00.000000', b'1', 2.00, 1, 1, 113),
(2, 'Av. de la Libertad, 12, Dos Hermanas', '2026-04-07', '08:00:00.000000', b'1', 2.00, 1, 1, 113),
(3, 'Av. de la Libertad, 12, Dos Hermanas', '2026-04-09', '08:00:00.000000', b'1', 2.00, 1, 1, 113),
(4, 'Av. Reina Mercedes, s/n (campus)', '2026-04-06', '14:30:00.000000', b'0', 2.00, 1, 1, 113),
(5, 'C/ Mairena, 5, Alcalá de Guadaíra', '2026-04-06', '07:45:00.000000', b'1', 1.50, 1, 2, 88),
(6, 'C/ Mairena, 5, Alcalá de Guadaíra', '2026-04-08', '07:45:00.000000', b'1', 1.50, 1, 2, 88),
(7, 'C/ Mairena, 5, Alcalá de Guadaíra', '2026-04-10', '07:45:00.000000', b'1', 1.50, 1, 2, 88),
(8, 'C/ Real, 20, Mairena del Aljarafe', '2026-04-07', '08:15:00.000000', b'1', 1.00, 1, 3, 41),
(9, 'C/ Real, 20, Mairena del Aljarafe', '2026-04-11', '08:15:00.000000', b'1', 1.00, 1, 3, 41),
(10, 'Av. Reina Mercedes, s/n (campus)', '2026-04-07', '15:00:00.000000', b'0', 1.00, 1, 3, 41),
(11, 'Plaza de Abastos, 3, Carmona', '2026-04-06', '07:30:00.000000', b'1', 3.00, 4, 4, 83),
(12, 'Plaza de Abastos, 3, Carmona', '2026-04-08', '07:30:00.000000', b'1', 3.00, 4, 4, 83),
(13, 'Plaza de Abastos, 3, Carmona', '2026-04-12', '07:30:00.000000', b'1', 3.00, 4, 4, 83),
(14, 'C/ Larga, 8, Los Palacios y Villafranca', '2026-04-06', '08:30:00.000000', b'1', 2.50, 1, 5, 115),
(15, 'C/ Larga, 8, Los Palacios y Villafranca', '2026-04-09', '08:30:00.000000', b'1', 2.50, 1, 5, 115),
(16, 'C/ Larga, 8, Los Palacios y Villafranca', '2026-04-13', '08:30:00.000000', b'1', 2.50, 1, 5, 115),
(17, 'Av. Reina Mercedes, s/n (campus)', '2026-04-09', '14:00:00.000000', b'0', 2.50, 1, 5, 115);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trip_passenger`
--

CREATE TABLE `trip_passenger` (
  `trip_id` bigint(20) NOT NULL,
  `passenger_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trip_requester`
--

CREATE TABLE `trip_requester` (
  `trip_id` bigint(20) NOT NULL,
  `requester_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `banned` bit(1) NOT NULL,
  `birthdate` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `driving_license_year` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(9) NOT NULL,
  `profile_image_url` longtext DEFAULT NULL,
  `strikes` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `home_town_id` bigint(20) NOT NULL,
  `usual_campus_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `banned`, `birthdate`, `description`, `driving_license_year`, `email`, `genre`, `name`, `password`, `phone`, `profile_image_url`, `strikes`, `username`, `home_town_id`, `usual_campus_id`) VALUES
(1, b'0', '1999-03-15', 'Conduzco todos los días al campus de Reina Mercedes.', 2018, 'juanma@unicar.es', 'M', 'Juan Manuel García', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123001', NULL, 0, 'juanma_dh', 113, 1),
(2, b'0', '2000-07-22', 'Informática 3º. Salgo a las 8h desde Alcalá.', 2019, 'laura@unicar.es', 'F', 'Laura Pérez Ruiz', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123002', NULL, 0, 'laura_alcala', 88, 1),
(3, b'0', '1998-11-05', 'Voy a Reina Mercedes desde Mairena. Coche grande.', 2017, 'carlos@unicar.es', 'M', 'Carlos López Vega', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123003', NULL, 0, 'carlos_mairena', 41, 1),
(4, b'0', '2001-01-30', 'Derecho en Ramón y Cajal. Salgo de Carmona a las 7:45.', 2020, 'sofia@unicar.es', 'F', 'Sofía Martínez Blanco', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123004', NULL, 0, 'sofia_carmona', 83, 4),
(5, b'0', '1999-09-12', 'Ingeniería Informática. Viajo desde Los Palacios cada día.', 2018, 'pablo@unicar.es', 'M', 'Pablo Fernández Mora', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123005', NULL, 0, 'pablo_palacios', 115, 1),
(6, b'0', '2002-05-18', 'Busco viaje desde Triana o alrededores.', NULL, 'ana@unicar.es', 'F', 'Ana Sánchez Torres', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '654123006', NULL, 0, 'ana_triana', 10, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `campus`
--
ALTER TABLE `campus`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_car_user` (`user_id`);

--
-- Indices de la tabla `rating`
--
ALTER TABLE `rating`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_rating_rated_user` (`rated_user_id`),
  ADD KEY `fk_rating_user_rate` (`user_rate_id`);

--
-- Indices de la tabla `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_reported_user` (`reported_user_id`),
  ADD KEY `fk_user_report` (`user_report_id`);

--
-- Indices de la tabla `town`
--
ALTER TABLE `town`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKo42o239nesjwxl64dbtfpwd6b` (`zip_code`);

--
-- Indices de la tabla `trip`
--
ALTER TABLE `trip`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_trip_campus` (`campus_id`),
  ADD KEY `fk_trip_car` (`car_id`),
  ADD KEY `fk_trip_town` (`town_id`);

--
-- Indices de la tabla `trip_passenger`
--
ALTER TABLE `trip_passenger`
  ADD PRIMARY KEY (`trip_id`,`passenger_id`),
  ADD KEY `FKspcfncy48n1g6sc29m6f8lnpj` (`passenger_id`);

--
-- Indices de la tabla `trip_requester`
--
ALTER TABLE `trip_requester`
  ADD PRIMARY KEY (`trip_id`,`requester_id`),
  ADD KEY `FKp4s46v9mb2uafxquvba9cv43` (`requester_id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`),
  ADD KEY `fk_user_town` (`home_town_id`),
  ADD KEY `fk_user_campus` (`usual_campus_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `campus`
--
ALTER TABLE `campus`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `car`
--
ALTER TABLE `car`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `rating`
--
ALTER TABLE `rating`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `report`
--
ALTER TABLE `report`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `town`
--
ALTER TABLE `town`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=133;

--
-- AUTO_INCREMENT de la tabla `trip`
--
ALTER TABLE `trip`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `fk_car_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `rating`
--
ALTER TABLE `rating`
  ADD CONSTRAINT `fk_rating_rated_user` FOREIGN KEY (`rated_user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `fk_rating_user_rate` FOREIGN KEY (`user_rate_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `fk_reported_user` FOREIGN KEY (`reported_user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `fk_user_report` FOREIGN KEY (`user_report_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `trip`
--
ALTER TABLE `trip`
  ADD CONSTRAINT `fk_trip_campus` FOREIGN KEY (`campus_id`) REFERENCES `campus` (`id`),
  ADD CONSTRAINT `fk_trip_car` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
  ADD CONSTRAINT `fk_trip_town` FOREIGN KEY (`town_id`) REFERENCES `town` (`id`);

--
-- Filtros para la tabla `trip_passenger`
--
ALTER TABLE `trip_passenger`
  ADD CONSTRAINT `FKg2k3r4stf0rjceouge6iidmed` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`),
  ADD CONSTRAINT `FKspcfncy48n1g6sc29m6f8lnpj` FOREIGN KEY (`passenger_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `trip_requester`
--
ALTER TABLE `trip_requester`
  ADD CONSTRAINT `FKg103kjyask1jgwgxwigcjquab` FOREIGN KEY (`trip_id`) REFERENCES `trip` (`id`),
  ADD CONSTRAINT `FKp4s46v9mb2uafxquvba9cv43` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_user_campus` FOREIGN KEY (`usual_campus_id`) REFERENCES `campus` (`id`),
  ADD CONSTRAINT `fk_user_town` FOREIGN KEY (`home_town_id`) REFERENCES `town` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
