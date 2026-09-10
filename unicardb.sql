-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-05-2026 a las 20:56:13
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
(1, 'Avda. Reina Mercedes, s/n', 'Reina Mercedes'),
(2, 'Avda. Ramón y Cajal / Calle Enramadilla', 'Viapol / Ramón y Cajal'),
(3, 'Camino de los Descubrimientos, s/n', 'Cartuja'),
(4, 'Avda. Sánchez Pizjuán, s/n', 'Macarena'),
(5, 'Calle Virgen de África, 7', 'Politécnico'),
(6, 'Calle San Fernando, 4', 'Rectorado / Centro'),
(7, 'Autovía A-376, km. 1', 'UPO (Pablo de Olavide)'),
(8, 'Calle Laraña, 3', 'Bellas Artes');

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
(1, 5, 'Rojo', '1234CBC', 'Seat Ibiza', 1),
(2, 4, 'Azul', '5678 DDF', 'Ford Fiesta', 2),
(3, 5, 'Negro', '9101GHF', 'Volkswagen Golf', 2),
(4, 7, 'Blanco', '1122JKL', 'Renault Scenic', 4),
(5, 5, 'Gris', '3344MNP', 'Peugeot 308', 3),
(6, 2, 'Amarillo', '5566PQR', 'Smart Fortwo', 5),
(7, 5, 'Verde', 'N-7788-P', 'Toyota Corolla', 3),
(8, 5, 'Azul oscuro', 'CD-1122-EF', 'Honda Civic', 6),
(9, 5, 'Rojo', '7788STC', 'Mazda 3', 7),
(10, 4, 'Negro', 'FG-3456-HJ', 'Opel Corsa', 1),
(11, 5, 'Blanco', 'KL-5678-MN', 'Hyundai i30', 6),
(12, 7, 'Gris plata', 'PQ-7890-ST', 'Dacia Duster', 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favorite`
--

CREATE TABLE `favorite` (
  `id` bigint(20) NOT NULL,
  `favorite_user_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `favorite`
--

INSERT INTO `favorite` (`id`, `favorite_user_id`, `user_id`) VALUES
(6, 4, 2),
(5, 5, 2),
(2, 6, 2),
(4, 7, 2),
(7, 3, 4),
(3, 2, 6),
(1, 8, 6);

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

--
-- Volcado de datos para la tabla `rating`
--

INSERT INTO `rating` (`id`, `rating`, `rated_user_id`, `user_rate_id`) VALUES
(1, 3, 6, 2),
(2, 4, 2, 6),
(3, 4, 7, 4),
(4, 3, 5, 4),
(5, 2, 6, 4),
(6, 1, 2, 4),
(7, 4, 8, 4),
(8, 5, 3, 4),
(9, 5, 5, 2),
(10, 3, 4, 2),
(11, 5, 3, 2),
(12, 5, 7, 2),
(13, 4, 8, 2);

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

--
-- Volcado de datos para la tabla `report`
--

INSERT INTO `report` (`id`, `date`, `reason`, `reported_user_id`, `user_report_id`) VALUES
(1, '2026-05-14', 's', 2, 6),
(2, '2026-05-14', 'Fantasma', 2, 4),
(3, '2026-05-14', 'Corrupto', 2, 4);

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
(1, 'Sevilla', '41001'),
(2, 'Coria del Río', '41100'),
(3, 'Bollullos de la Mitación', '41110'),
(4, 'Almensilla', '41111'),
(5, 'Gelves', '41120'),
(6, 'La Puebla del Río', '41130'),
(7, 'Isla Mayor', '41140'),
(8, 'Alcalá del Río', '41200'),
(9, 'Burguillos', '41209'),
(10, 'Guillena', '41210'),
(11, 'Castilblanco de los Arroyos', '41230'),
(12, 'Almadén de la Plata', '41240'),
(13, 'El Real de la Jara', '41250'),
(14, 'San José de la Rinconada', '41300'),
(15, 'La Rinconada', '41309'),
(16, 'Brenes', '41310'),
(17, 'Villaverde del Río', '41318'),
(18, 'Cantillana', '41320'),
(19, 'Villanueva del Río y Minas', '41350'),
(20, 'El Pedroso', '41360'),
(21, 'Cazalla de la Sierra', '41370'),
(22, 'Alanís', '41380'),
(23, 'San Nicolás del Puerto', '41388'),
(24, 'Guadalcanal', '41390'),
(25, 'Écija', '41400'),
(26, 'Carmona', '41410'),
(27, 'Fuentes de Andalucía', '41420'),
(28, 'La Campana', '41429'),
(29, 'La Luisiana', '41430'),
(30, 'Cañada Rosal', '41439'),
(31, 'Lora del Río', '41440'),
(32, 'Alcolea del Río', '41449'),
(33, 'Constantina', '41450'),
(34, 'Las Navas de la Concepción', '41460'),
(35, 'Peñaflor', '41470'),
(36, 'La Puebla de los Infantes', '41479'),
(37, 'Alcalá de Guadaíra', '41500'),
(38, 'Mairena del Alcor', '41510'),
(39, 'El Viso del Alcor', '41520'),
(40, 'Morón de la Frontera', '41530'),
(41, 'La Puebla de Cazalla', '41540'),
(42, 'Aguadulce', '41550'),
(43, 'Estepa', '41560'),
(44, 'Lora de Estepa', '41564'),
(45, 'Gilena', '41565'),
(46, 'Pedrera', '41566'),
(47, 'Herrera', '41567'),
(48, 'El Rubio', '41568'),
(49, 'Marinaleda', '41569'),
(50, 'La Roda de Andalucía', '41590'),
(51, 'Arahal', '41600'),
(52, 'Paradas', '41610'),
(53, 'Marchena', '41620'),
(54, 'Osuna', '41630'),
(55, 'El Saucejo', '41650'),
(56, 'Los Corrales', '41657'),
(57, 'Martín de la Jara', '41658'),
(58, 'Villanueva de San Juan', '41660'),
(59, 'Algámitas', '41661'),
(60, 'Pruna', '41670'),
(61, 'Dos Hermanas', '41700'),
(62, 'Utrera', '41710'),
(63, 'El Palmar de Troya', '41719'),
(64, 'Los Palacios y Villafranca', '41720'),
(65, 'Las Cabezas de San Juan', '41730'),
(66, 'Lebrija', '41740'),
(67, 'El Cuervo de Sevilla', '41749'),
(68, 'Montellano', '41750'),
(69, 'El Coronil', '41760'),
(70, 'Coripe', '41770'),
(71, 'Sanlúcar la Mayor', '41800'),
(72, 'Olivares', '41804'),
(73, 'Benacazón', '41805'),
(74, 'Umbrete', '41806'),
(75, 'Espartinas', '41807'),
(76, 'Salteras', '41808'),
(77, 'Albaida del Aljarafe', '41809'),
(78, 'Castilleja del Campo', '41810'),
(79, 'Carrión de los Céspedes', '41820'),
(80, 'Huévar del Aljarafe', '41830'),
(81, 'Pilas', '41840'),
(82, 'Aznalcázar', '41849'),
(83, 'Villamanrique de la Condesa', '41850'),
(84, 'Gerena', '41860'),
(85, 'Aznalcóllar', '41870'),
(86, 'El Ronquillo', '41880'),
(87, 'El Garrobo', '41889'),
(88, 'El Castillo de las Guardas', '41890'),
(89, 'El Madroño', '41897'),
(90, 'Camas', '41900'),
(91, 'Valencina de la Concepción', '41907'),
(92, 'Castilleja de Guzmán', '41908'),
(93, 'San Juan de Aznalfarache', '41920'),
(94, 'Mairena del Aljarafe', '41927'),
(95, 'Palomares del Río', '41928'),
(96, 'Bormujos', '41930'),
(97, 'Tomares', '41940'),
(98, 'Castilleja de la Cuesta', '41950'),
(99, 'Gines', '41960'),
(100, 'Santiponce', '41970'),
(101, 'La Algaba', '41980');

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
(1, 'Instituto', '2026-06-01', '08:30:00.000000', b'1', 2.05, 3, 3, 9),
(2, 'Colegio', '2026-06-02', '07:45:00.000000', b'1', 3.15, 7, 11, 12),
(3, 'Estación Central', '2026-06-03', '07:15:00.000000', b'1', 2.50, 3, 1, 14),
(4, 'Plaza Mayor', '2026-06-08', '13:40:00.000000', b'0', 1.75, 5, 2, 27),
(5, 'Avenida Libertad', '2026-06-15', '18:20:00.000000', b'0', 4.10, 1, 3, 39),
(6, 'Biblioteca Municipal', '2026-06-22', '09:05:00.000000', b'1', 0.95, 8, 4, 52),
(7, 'Centro Comercial', '2026-07-01', '20:10:00.000000', b'0', 5.65, 2, 5, 63),
(8, 'Parque Norte', '2026-07-07', '06:30:00.000000', b'0', 3.30, 6, 6, 71),
(9, 'Instituto Tecnológico', '2026-07-12', '14:55:00.000000', b'1', 6.95, 4, 7, 84),
(10, 'Calle del Sol', '2026-07-18', '11:45:00.000000', b'0', 2.15, 7, 8, 90),
(11, 'Puerto Deportivo', '2026-07-23', '16:25:00.000000', b'0', 0.50, 5, 9, 97),
(12, 'Hospital General', '2026-07-29', '08:50:00.000000', b'1', 4.75, 1, 10, 100),
(13, 'Polideportivo', '2026-08-01', '19:35:00.000000', b'0', 1.20, 3, 12, 45);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trip_passenger`
--

CREATE TABLE `trip_passenger` (
  `trip_id` bigint(20) NOT NULL,
  `passenger_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `trip_passenger`
--

INSERT INTO `trip_passenger` (`trip_id`, `passenger_id`) VALUES
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(2, 2),
(2, 3),
(2, 4),
(2, 8),
(3, 2),
(3, 6),
(3, 8),
(4, 5),
(4, 6),
(4, 7),
(5, 3),
(5, 4),
(5, 5),
(5, 6),
(5, 7),
(6, 2),
(6, 5),
(6, 6),
(6, 7),
(6, 8),
(7, 4),
(7, 6),
(7, 7),
(8, 2),
(8, 7),
(9, 2),
(9, 4),
(9, 5),
(9, 7),
(10, 2),
(10, 3),
(10, 5),
(10, 7),
(10, 8),
(11, 2),
(11, 6),
(12, 3),
(12, 4),
(12, 5),
(12, 8),
(13, 2),
(13, 4),
(13, 6),
(13, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trip_requester`
--

CREATE TABLE `trip_requester` (
  `trip_id` bigint(20) NOT NULL,
  `requester_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `trip_requester`
--

INSERT INTO `trip_requester` (`trip_id`, `requester_id`) VALUES
(1, 3),
(1, 8),
(2, 5),
(2, 7),
(3, 3),
(3, 4),
(3, 5),
(3, 7),
(4, 3),
(4, 4),
(4, 8),
(6, 3),
(7, 2),
(7, 5),
(7, 8),
(8, 6),
(9, 6),
(9, 8),
(10, 4),
(11, 3),
(11, 4),
(11, 5),
(11, 8),
(12, 2),
(12, 6),
(12, 7),
(13, 3),
(13, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `banned` bit(1) NOT NULL,
  `birthdate` date NOT NULL,
  `description` text DEFAULT NULL,
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
  `usual_campus_id` bigint(20) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `banned`, `birthdate`, `description`, `driving_license_year`, `email`, `genre`, `name`, `password`, `phone`, `profile_image_url`, `strikes`, `username`, `home_town_id`, `usual_campus_id`, `role`) VALUES
(1, b'0', '2000-01-01', '', 2018, 'Usuario001@gmail.com', 'Hombre', 'Usuario Test 001', '$2a$10$re3DNC0KcbMZXENEXdpkh.1OTsYhSyJqifIxng73IC0xWZy8j0ieu', '600001001', NULL, 1, 'Usuario001', 92, 8, 'USER'),
(2, b'0', '2001-01-01', '', 2019, 'Admin001@gmail.com', 'Hombre', 'Usuario Admin 001', '$2a$10$VjIy5Enm0jo/NPDyfEIL9O/fSCxGCgewfzkS8sORIEaATCtYEm26C', '700001001', '', 1, 'Admin001', 96, 8, 'ADMIN'),
(3, b'0', '2002-02-02', '', 2020, 'Usuario002@gmail.com', 'Mujer', 'Usuario Test 002', '$2a$10$zAcRCvh8nM5rWDPq/WijK.lAW7BHESLpmsFfLpOKJJmjKOCMqCBaK', '600002002', NULL, 0, 'Usuario002', 11, 5, 'USER'),
(4, b'0', '2003-03-03', '', 2021, 'Usuario003@gmail.com', 'Hombre', 'Usuario Test 003', '$2a$10$7xEV9pUMitMMYpXGrma1e.M8aWVvPjgzTZ.ifbkc861IJ7qFaCM.C', '600003003', NULL, 0, 'Usuario003', 32, 6, 'USER'),
(5, b'0', '2004-04-04', '', 2022, 'Usuario004@gmail.com', 'Mujer', 'Usuario Test 004', '$2a$10$8xImPuiFPm3Lexl85vjZJ.SkHvm56Ukj.qBLZGQUnraww7kZwquO2', '600004004', NULL, 0, 'Usuario004', 30, 4, 'USER'),
(6, b'0', '2005-05-05', '', 2023, 'Usuario005@gmail.com', 'Mujer', 'Usuario Test 005', '$2a$10$n94lhmMwaZ6m4ezFJ5RejuArITVkTX5Jk7Yb6VWoSEaFKov/rEYHG', '600005005', NULL, 0, 'Usuario005', 51, 4, 'USER'),
(7, b'0', '2006-06-06', '', 2024, 'Usuario006@gmail.com', 'No binario', 'Usuario Test 006', '$2a$10$GGWvXemS.93sMvRuBjtS6ebI58CKbODH8OuvbvrBnSG99eoTBOUze', '600006006', NULL, 0, 'Usuario006', 13, 4, 'USER'),
(8, b'0', '2012-02-02', '', 2025, 'Admin002@gmail.com', 'Hombre', 'Usuario Admin 002', '$2a$10$rYt60n0f/WgE.CFUpAIj0e6fpXu1qJs9jQzUkBKi4ACq3K3xeMuJS', '700002002', NULL, 0, 'Admin002', 29, 7, 'ADMIN');

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
-- Indices de la tabla `favorite`
--
ALTER TABLE `favorite`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK1dolp59p8ipbychpmp8ktnitg` (`user_id`,`favorite_user_id`),
  ADD KEY `fk_favorite_target` (`favorite_user_id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `car`
--
ALTER TABLE `car`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `favorite`
--
ALTER TABLE `favorite`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `rating`
--
ALTER TABLE `rating`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `report`
--
ALTER TABLE `report`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `town`
--
ALTER TABLE `town`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102;

--
-- AUTO_INCREMENT de la tabla `trip`
--
ALTER TABLE `trip`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `fk_car_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Filtros para la tabla `favorite`
--
ALTER TABLE `favorite`
  ADD CONSTRAINT `fk_favorite_target` FOREIGN KEY (`favorite_user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

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

-- =====================================================================
--  AMPLIACION PARA PRUEBAS (viajes periodicos + datos con fechas lejanas)
--  Anadido el 2026-09-08. Contrasena de todos los usuarios *Test: Test1234!
-- =====================================================================

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `periodic_trip`
-- (la genera Hibernate con ddl-auto=update; se crea aqui para poder
--  sembrar datos en el arranque del contenedor de base de datos)
-- --------------------------------------------------------

CREATE TABLE `periodic_trip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `departure_address` varchar(255) NOT NULL,
  `departure_time` time(6) NOT NULL,
  `end_date` date NOT NULL,
  `is_to_campus` bit(1) NOT NULL,
  `price` decimal(4,2) NOT NULL,
  `repeat_interval_weeks` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `car_id` bigint(20) NOT NULL,
  `campus_id` bigint(20) NOT NULL,
  `town_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_periodic_trip_car` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
  CONSTRAINT `fk_periodic_trip_campus` FOREIGN KEY (`campus_id`) REFERENCES `campus` (`id`),
  CONSTRAINT `fk_periodic_trip_town` FOREIGN KEY (`town_id`) REFERENCES `town` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `periodic_trip_days` (
  `periodic_trip_id` bigint(20) NOT NULL,
  `day_of_week` varchar(255) DEFAULT NULL,
  KEY `idx_ptd_ptid` (`periodic_trip_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- FK nullable de trip hacia su plantilla periodica
ALTER TABLE `trip`
  ADD COLUMN `periodic_trip_id` bigint(20) DEFAULT NULL,
  ADD CONSTRAINT `fk_trip_periodic_trip` FOREIGN KEY (`periodic_trip_id`) REFERENCES `periodic_trip` (`id`) ON DELETE SET NULL;

-- --------------------------------------------------------
-- Usuarios de prueba (id 9-16). Password en claro: Test1234!
-- --------------------------------------------------------

INSERT INTO `user` (`id`, `banned`, `birthdate`, `description`, `driving_license_year`, `email`, `genre`, `name`, `password`, `phone`, `profile_image_url`, `strikes`, `username`, `home_town_id`, `usual_campus_id`, `role`) VALUES
(9,  b'0', '1999-04-12', 'Conductora habitual ruta Aljarafe', 2017, 'lucia.test@unicar.dev',  'Mujer',      'Lucia Prueba',        '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000009', NULL, 0, 'luciaTest', 96,  3, 'USER'),
(10, b'0', '1998-11-23', 'Disponible por las mananas',        2016, 'marco.test@unicar.dev',  'Hombre',     'Marco Prueba',        '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000010', NULL, 0, 'marcoTest', 61,  1, 'USER'),
(11, b'0', '2001-07-05', '',                                  2020, 'sara.test@unicar.dev',   'Mujer',      'Sara Prueba',         '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000011', NULL, 0, 'saraTest',  37,  4, 'USER'),
(12, b'0', '2000-02-18', 'Voy a Cartuja casi todos los dias', 2019, 'diego.test@unicar.dev',  'Hombre',     'Diego Prueba',        '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000012', NULL, 0, 'diegoTest', 90,  3, 'USER'),
(13, b'0', '1997-09-30', '',                                  2015, 'noa.test@unicar.dev',    'No binario', 'Noa Prueba',          '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000013', NULL, 0, 'noaTest',   94,  5, 'USER'),
(14, b'0', '2002-05-14', 'Nueva en la plataforma',            NULL, 'irene.test@unicar.dev',  'Mujer',      'Irene Prueba',        '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000014', NULL, 0, 'ireneTest', 62,  2, 'USER'),
(15, b'0', '1996-12-01', 'Conductor los fines de semana',     2014, 'pablo.test@unicar.dev',  'Hombre',     'Pablo Prueba',        '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000015', NULL, 1, 'pabloTest', 100, 3, 'USER'),
(16, b'0', '1999-08-08', 'Cuenta de administracion de pruebas',2018, 'admin.test@unicar.dev',  'Mujer',      'Ada Prueba (Admin)',  '$2y$10$kQD1VPSVrLN/Dm3OZRZVkOEj.3wLzeZ7HYG9bjDySBYYZOzgUcH2S', '611000016', NULL, 0, 'adminTest', 1,   6, 'ADMIN');

-- --------------------------------------------------------
-- Coches de los usuarios de prueba (id 13-24)
-- --------------------------------------------------------

INSERT INTO `car` (`id`, `capacity`, `color`, `license_plate`, `model`, `user_id`) VALUES
(13, 4, 'Blanco',   '1001 KLM', 'Seat Leon',           9),
(14, 5, 'Gris',     '1002 KLM', 'Renault Megane',      10),
(15, 4, 'Azul',     '1003 KLM', 'Peugeot 208',         11),
(16, 5, 'Negro',    '1004 KLM', 'Volkswagen Polo',     12),
(17, 7, 'Rojo',     '1005 KLM', 'Citroen C4 Picasso',  13),
(18, 4, 'Verde',    '1006 KLM', 'Toyota Yaris',        14),
(19, 5, 'Blanco',   '1007 KLM', 'Kia Ceed',            15),
(20, 5, 'Plata',    '1008 KLM', 'Hyundai i20',         16),
(21, 2, 'Amarillo', '1009 KLM', 'Fiat 500',            9),
(22, 5, 'Azul',     '1010 KLM', 'Mazda 2',             10),
(23, 4, 'Gris',     '1011 KLM', 'Opel Astra',          11),
(24, 6, 'Negro',    '1012 KLM', 'Ford Focus SW',       12);

-- --------------------------------------------------------
-- Viajes sueltos con fechas lejanas (id 14-25)
-- --------------------------------------------------------

INSERT INTO `trip` (`id`, `departure_address`, `departure_date`, `departure_time`, `is_to_campus`, `price`, `campus_id`, `car_id`, `town_id`) VALUES
(14, 'Av. de Europa 12',      '2027-02-15', '07:50:00.000000', b'1', 2.20, 3, 13, 96),
(15, 'Plaza del Pueblo',      '2027-03-20', '08:10:00.000000', b'1', 1.80, 1, 14, 61),
(16, 'C/ Mayor 4',            '2027-04-10', '14:30:00.000000', b'0', 2.75, 4, 15, 37),
(17, 'Estacion de Cercanias', '2027-05-12', '07:30:00.000000', b'1', 3.40, 3, 16, 90),
(18, 'Rotonda Sur',           '2027-06-01', '18:15:00.000000', b'0', 1.50, 5, 17, 94),
(19, 'C/ Sevilla 20',         '2027-07-15', '09:00:00.000000', b'1', 4.10, 2, 18, 62),
(20, 'Parque Tecnologico',    '2027-09-21', '15:45:00.000000', b'0', 2.00, 3, 19, 100),
(21, 'Av. Blas Infante',      '2027-11-05', '08:25:00.000000', b'1', 3.15, 6, 20, 1),
(22, 'C/ Real 8',             '2027-12-18', '13:20:00.000000', b'0', 1.95, 3, 21, 96),
(23, 'Mercado de Abastos',    '2028-01-10', '07:40:00.000000', b'1', 2.60, 1, 22, 61),
(24, 'Poligono Industrial',   '2028-02-14', '16:05:00.000000', b'0', 5.20, 4, 23, 37),
(25, 'Av. de la Paz 30',      '2028-03-01', '08:00:00.000000', b'1', 2.35, 3, 24, 90);

-- --------------------------------------------------------
-- Plantillas de viajes periodicos (id 1-3)
-- --------------------------------------------------------

INSERT INTO `periodic_trip` (`id`, `departure_address`, `departure_time`, `end_date`, `is_to_campus`, `price`, `repeat_interval_weeks`, `start_date`, `car_id`, `campus_id`, `town_id`) VALUES
(1, 'Parada Autobus Norte', '07:45:00.000000', '2027-03-31', b'1', 2.50, 1, '2027-03-01', 17, 3, 96),
(2, 'Puerta Facultad B',    '14:15:00.000000', '2027-04-30', b'0', 3.00, 1, '2027-04-01', 19, 2, 62),
(3, 'Rotonda Este',         '08:05:00.000000', '2027-10-29', b'1', 1.75, 1, '2027-10-04', 24, 4, 37);

INSERT INTO `periodic_trip_days` (`periodic_trip_id`, `day_of_week`) VALUES
(1, 'MONDAY'),
(1, 'WEDNESDAY'),
(2, 'FRIDAY'),
(3, 'TUESDAY'),
(3, 'THURSDAY');

-- --------------------------------------------------------
-- Viajes individuales generados a partir de cada plantilla periodica
--   Periodica 1 -> lunes y miercoles de marzo 2027   (id 26-35)
--   Periodica 2 -> viernes de abril 2027             (id 36-40)
--   Periodica 3 -> martes y jueves de octubre 2027   (id 41-48)
-- --------------------------------------------------------

INSERT INTO `trip` (`id`, `departure_address`, `departure_date`, `departure_time`, `is_to_campus`, `price`, `campus_id`, `car_id`, `town_id`, `periodic_trip_id`) VALUES
(26, 'Parada Autobus Norte', '2027-03-01', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(27, 'Parada Autobus Norte', '2027-03-03', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(28, 'Parada Autobus Norte', '2027-03-08', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(29, 'Parada Autobus Norte', '2027-03-10', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(30, 'Parada Autobus Norte', '2027-03-15', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(31, 'Parada Autobus Norte', '2027-03-17', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(32, 'Parada Autobus Norte', '2027-03-22', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(33, 'Parada Autobus Norte', '2027-03-24', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(34, 'Parada Autobus Norte', '2027-03-29', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(35, 'Parada Autobus Norte', '2027-03-31', '07:45:00.000000', b'1', 2.50, 3, 17, 96, 1),
(36, 'Puerta Facultad B',    '2027-04-02', '14:15:00.000000', b'0', 3.00, 2, 19, 62, 2),
(37, 'Puerta Facultad B',    '2027-04-09', '14:15:00.000000', b'0', 3.00, 2, 19, 62, 2),
(38, 'Puerta Facultad B',    '2027-04-16', '14:15:00.000000', b'0', 3.00, 2, 19, 62, 2),
(39, 'Puerta Facultad B',    '2027-04-23', '14:15:00.000000', b'0', 3.00, 2, 19, 62, 2),
(40, 'Puerta Facultad B',    '2027-04-30', '14:15:00.000000', b'0', 3.00, 2, 19, 62, 2),
(41, 'Rotonda Este',         '2027-10-05', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(42, 'Rotonda Este',         '2027-10-07', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(43, 'Rotonda Este',         '2027-10-12', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(44, 'Rotonda Este',         '2027-10-14', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(45, 'Rotonda Este',         '2027-10-19', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(46, 'Rotonda Este',         '2027-10-21', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(47, 'Rotonda Este',         '2027-10-26', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3),
(48, 'Rotonda Este',         '2027-10-28', '08:05:00.000000', b'1', 1.75, 4, 24, 37, 3);

-- --------------------------------------------------------
-- Pasajeros confirmados y solicitantes en los nuevos viajes
-- --------------------------------------------------------

INSERT INTO `trip_passenger` (`trip_id`, `passenger_id`) VALUES
(14, 10), (14, 11), (14, 12),
(15, 9),  (15, 13),
(16, 12), (16, 14),
(17, 9),  (17, 10),
(20, 11), (20, 16),
(26, 9),  (26, 10),
(27, 11), (27, 12),
(36, 9),  (36, 14),
(41, 10), (41, 13);

INSERT INTO `trip_requester` (`trip_id`, `requester_id`) VALUES
(14, 13), (14, 14),
(20, 9),  (20, 10),
(26, 15),
(36, 11);

-- --------------------------------------------------------
-- Favoritos y valoraciones entre usuarios de prueba
-- --------------------------------------------------------

INSERT INTO `favorite` (`id`, `favorite_user_id`, `user_id`) VALUES
(9,  10, 9),
(10, 11, 9),
(11, 9,  10),
(12, 13, 12),
(13, 16, 15),
(14, 12, 11);

INSERT INTO `rating` (`id`, `rating`, `rated_user_id`, `user_rate_id`) VALUES
(14, 5, 10, 9),
(15, 4, 9,  10),
(16, 3, 11, 9),
(17, 5, 12, 11),
(18, 4, 9,  13),
(19, 2, 14, 12),
(20, 5, 16, 11),
(21, 4, 13, 10);

INSERT INTO `report` (`id`, `date`, `reason`, `reported_user_id`, `user_report_id`) VALUES
(4, '2027-02-20', 'No se presento al punto de encuentro', 12, 9),
(5, '2027-03-05', 'Conduccion temeraria', 15, 11);

-- --------------------------------------------------------
-- Ajuste de los contadores AUTO_INCREMENT
-- --------------------------------------------------------

ALTER TABLE `user`     AUTO_INCREMENT = 17;
ALTER TABLE `car`      AUTO_INCREMENT = 25;
ALTER TABLE `trip`     AUTO_INCREMENT = 49;
ALTER TABLE `favorite` AUTO_INCREMENT = 15;
ALTER TABLE `rating`   AUTO_INCREMENT = 22;
ALTER TABLE `report`   AUTO_INCREMENT = 6;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
