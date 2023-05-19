-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-05-2023 a las 01:30:53
-- Versión del servidor: 10.4.24-MariaDB
-- Versión de PHP: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `trabajos_de_grado_unisangil`
--
CREATE DATABASE IF NOT EXISTS `trabajos_de_grado_unisangil` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `trabajos_de_grado_unisangil`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasechangelog`
--

CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databasechangelog`
--

INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2023-05-01 18:40:36', 1, 'EXECUTED', '8:fc4cef322f7a2e170faaa8b025375893', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '4.15.0', NULL, NULL, '2984436232'),
('20230502230843-1', 'jhipster', 'config/liquibase/changelog/20230502230843_added_entity_Proposal.xml', '2023-05-02 18:31:33', 2, 'EXECUTED', '8:5f4dc66f1bf98335e2767fc0e497dc24', 'createTable tableName=proposal', '', NULL, '4.15.0', NULL, NULL, '3070293197'),
('20230502230843-1-data', 'jhipster', 'config/liquibase/changelog/20230502230843_added_entity_Proposal.xml', '2023-05-02 18:31:33', 3, 'EXECUTED', '8:24bba059aaa5d7e1d8f73dfff910bed1', 'loadData tableName=proposal', '', NULL, '4.15.0', 'faker', NULL, '3070293197'),
('20230502230844-1', 'jhipster', 'config/liquibase/changelog/20230502230844_added_entity_Modality.xml', '2023-05-02 18:31:33', 4, 'EXECUTED', '8:7ff140e8c8bc4fe9ad096f8bd6f7b093', 'createTable tableName=modality', '', NULL, '4.15.0', NULL, NULL, '3070293197'),
('20230502230844-1-data', 'jhipster', 'config/liquibase/changelog/20230502230844_added_entity_Modality.xml', '2023-05-02 18:31:33', 5, 'EXECUTED', '8:7639e55f3b65f3b931939c20a2c7e064', 'loadData tableName=modality', '', NULL, '4.15.0', 'faker', NULL, '3070293197');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasechangeloglock`
--

CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databasechangeloglock`
--

INSERT INTO `databasechangeloglock` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
(1, b'0', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jhi_authority`
--

CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `jhi_authority`
--

INSERT INTO `jhi_authority` (`name`) VALUES
('ROLE_ADMIN'),
('ROLE_DOCENTE'),
('ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jhi_user`
--

CREATE TABLE `jhi_user` (
  `id` bigint(20) NOT NULL,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(10) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `jhi_user`
--

INSERT INTO `jhi_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `image_url`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`) VALUES
(1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', b'1', 'es', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', b'1', 'es', NULL, NULL, 'system', NULL, NULL, 'system', NULL),
(3, 'juankestevez', '$2a$10$Pamalbf0q1F0F0z/46pGP./NqU4KLZHRwynSaiUVSApUv4Br2doxG', NULL, NULL, 'juank2001estevezvargas@gmail.com', NULL, b'1', 'es', '5hJ2UxR0rbdHt6Y2LRDt', 'rO9KgA1HppIJbvAOXMcE', 'anonymousUser', '2023-05-18 00:17:03', '2023-05-18 00:21:21', 'anonymousUser', '2023-05-18 00:21:21');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jhi_user_authority`
--

CREATE TABLE `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `jhi_user_authority`
--

INSERT INTO `jhi_user_authority` (`user_id`, `authority_name`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_DOCENTE'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(3, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `modality`
--

CREATE TABLE `modality` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `document` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Task entity.\\n@author The JHipster team.';

--
-- Volcado de datos para la tabla `modality`
--

INSERT INTO `modality` (`id`, `name`, `description`, `document`) VALUES
(2, 'withdrawal Cataluña Estonia', 'protocol', 'SMTP'),
(3, 'AGP', 'Agente', 'Granito'),
(4, 'logística Gráfica Pollo', 'Taiwan', 'synthesizing'),
(5, 'Gris Extramuros Rojo', 'Plástico index RSS', 'Pantalones'),
(6, 'Toallas Refinado', 'Rústico Auto', 'Coche Investment Murcia'),
(7, 'Buckinghamshire', 'digital Extramuros', 'Comunicaciones'),
(8, 'copying', 'Director', 'hardware Loan Ronda'),
(9, 'Azul', 'Mobilidad Indian', 'Galicia Vatu'),
(10, 'Granito calculating Principado', 'XSS Borders', 'Increible analista Pequeño');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proposal`
--

CREATE TABLE `proposal` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `archive` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `student` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='not an ignored comment';

--
-- Volcado de datos para la tabla `proposal`
--

INSERT INTO `proposal` (`id`, `title`, `archive`, `state`, `comments`, `student`) VALUES
(1, 'Coche', 'Hecho', 'APROBADA', 'Datos payment', 'disintermediate Artesanal engage'),
(2, 'Account', 'reboot', 'APROBADA', 'Usabilidad Analista', 'compress'),
(3, 'card Azul Representante', 'Mascotas Glorieta', 'CONDICIONADA', 'Operaciones Refinado Investment', 'Guapo Marroquinería'),
(4, 'mindshare strategize', 'Granito Prolongación Account', 'CONDICIONADA', 'Islands Seguro', 'Reactivo Futuro Amarillo'),
(5, 'Quinta Hormigon Violeta', 'Account', 'RECHAZADA', 'Terrenos desafío Mejorado', 'Cambridgeshire Configuración'),
(6, 'withdrawal AGP', 'Maldivas medición', 'RECHAZADA', 'Ergonómico', 'Identidad infrastructures'),
(7, 'experiences groupware', 'Mesa Sudán', 'CONDICIONADA', 'convergence Metal', 'open-source'),
(8, 'protocol', 'Jefe Parafarmacia navigate', 'APROBADA', 'Negro', 'digital neural'),
(9, 'TCP Belarussian', 'Rand Planificador', 'RECHAZADA', 'PNG relationships política', 'bypass'),
(10, 'Coche Camiseta', 'emulación navigate withdrawal', 'APROBADA', 'Municipio Granito', 'Ronda Práctico');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tutor`
--

CREATE TABLE `tutor` (
  `id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(250) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `hire_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tutor`
--

INSERT INTO `tutor` (`id`, `first_name`, `last_name`, `email`, `phone_number`, `hire_date`) VALUES
(1, 'tutor', 'tutor', 'tut', '7634521627', '2023-05-03 21:57:00');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `databasechangeloglock`
--
ALTER TABLE `databasechangeloglock`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `jhi_authority`
--
ALTER TABLE `jhi_authority`
  ADD PRIMARY KEY (`name`);

--
-- Indices de la tabla `jhi_user`
--
ALTER TABLE `jhi_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_user_login` (`login`),
  ADD UNIQUE KEY `ux_user_email` (`email`);

--
-- Indices de la tabla `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD PRIMARY KEY (`user_id`,`authority_name`),
  ADD KEY `fk_authority_name` (`authority_name`);

--
-- Indices de la tabla `modality`
--
ALTER TABLE `modality`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `proposal`
--
ALTER TABLE `proposal`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tutor`
--
ALTER TABLE `tutor`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `jhi_user`
--
ALTER TABLE `jhi_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `modality`
--
ALTER TABLE `modality`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `proposal`
--
ALTER TABLE `proposal`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `tutor`
--
ALTER TABLE `tutor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
