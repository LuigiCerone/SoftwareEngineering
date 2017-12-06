SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `Gaming`
--
CREATE DATABASE IF NOT EXISTS `Gaming`;
USE `Gaming`;

-- --------------------------------------------------------

--
-- Struttura della tabella `gioco`
--

DROP TABLE IF EXISTS `gioco`;
CREATE TABLE `gioco` (
  `id` int(6) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) NOT NULL,
  `punti` int(3) NOT NULL DEFAULT '10' COMMENT 'punti xp dati dal gioco'
) ENGINE=InnoDB;
