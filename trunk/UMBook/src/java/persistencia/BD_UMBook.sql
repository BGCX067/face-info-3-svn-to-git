SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Base de datos: `umbook`
--
CREATE DATABASE if not exists `umbook` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `umbook`;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `album`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `album` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idUsuario_2` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `ciudad`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `ciudad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Volcar la base de datos para la tabla `ciudad`
--

INSERT INTO `ciudad` (`id`, `nombre`) VALUES
(1, 'Capital'),
(2, 'Guaymallen'),
(3, 'San Martin'),
(4, 'Las Heras'),
(5, 'Godoy Cruz');

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `comentario`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `comentario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idUsuario` (`idUsuario`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `foto`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `foto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idAlbum` int(11) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `ubicacion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idAlbum` (`idAlbum`),
  KEY `idAlbum_2` (`idAlbum`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------
-- Estructura de tabla para la tabla `foto_comentario`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `foto_comentario` (
  `idFoto` int(11) NOT NULL,
  `idComentario` int(11) NOT NULL,
  PRIMARY KEY (`idComentario`),
  UNIQUE KEY `idComentario_2` (`idComentario`),
  KEY `idFoto` (`idFoto`,`idComentario`),
  KEY `idComentario` (`idComentario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `grupo`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `grupo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idUsuario` (`idUsuario`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------
-- Estructura de tabla para la tabla `grupo_usuario`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `grupo_usuario` (
  `idGrupo` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `estado` boolean,
  PRIMARY KEY (`idGrupo`,`idUsuario`),
  KEY `idUsuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------
-- Estructura de tabla para la tabla `localidad`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `localidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idCiudad` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idCiudad` (`idCiudad`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Volcar la base de datos para la tabla `localidad`
--

INSERT INTO `localidad` (`id`, `idCiudad`, `nombre`) VALUES
(1, 1, 'ciudad'),
(2, 2, 'Dorrego'),
(3, 5, 'Las Tortugas'),
(4, 5, 'Benegas'),
(5, 3, 'Alto Verde'),
(6, 3, 'Palmira'),
(7, 3, 'Montecaseros'),
(8, 3, 'Chivilcoy'),
(9, 2, 'San Jose'),
(10, 2, 'Los Corralitos'),
(11, 2, 'Villa Nueva'),
(12, 2, 'Bermejo'),
(13, 1, '4ta seccion'),
(14, 1, '6ta seccion'),
(15, 1, 'mendoza'),
(16, 1, '5ta seccion'),
(17, 4, 'Uspallata'),
(18, 4, 'Puente del INCA'),
(19, 4, 'Papagayos'),
(20, 4, 'Penitentes'),
(21, 4, 'El Challao'),
(22, 4, 'Blanco Encalada');

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `muro`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `muro` (
  `idUsuario` int(11) NOT NULL,
  `idComentario` int(11) NOT NULL,
  PRIMARY KEY (`idComentario`),
  UNIQUE KEY `idComentario` (`idComentario`),
  KEY `idUsuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `permiso`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `permiso` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(10) NOT NULL,
  `acceso` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `permiso_album`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `permiso_album` (
  `idAlbum` int(11) NOT NULL,
  `idPermiso` int(11) NOT NULL,
  `idGrupo` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idAlbum`,`idPermiso`,`idGrupo`),
  KEY `idPermiso` (`idPermiso`),
  KEY `idGrupo` (`idGrupo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `permiso_muro`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `permiso_muro` (
  `idUsuario` int(11) NOT NULL,
  `idPermiso` int(11) NOT NULL,
  `idGrupo` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUsuario`,`idPermiso`,`idGrupo`),
  KEY `idGrupo` (`idGrupo`),
  KEY `idGrupo_2` (`idGrupo`),
  KEY `idGrupo_3` (`idGrupo`),
  KEY `idPermiso` (`idPermiso`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `usuario`
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail` varchar(50) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `clave` varchar(50) NOT NULL,
  `idLocalidad` int(11) NOT NULL,
  `idCiudad` int(11) NOT NULL,
  `sexo` tinyint(1) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `celular` varchar(25) DEFAULT NULL,
  `fechaNac` date NOT NULL,
  `carrera` varchar(30) DEFAULT NULL,
  `rol` varchar(15) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `fotoPerfil` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mail` (`mail`),
  UNIQUE KEY `mail_2` (`mail`),
  KEY `fotoPerfil` (`fotoPerfil`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcar la base de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `mail`, `nombre`, `apellido`, `clave`, `idLocalidad`, `idCiudad`, `sexo`, `telefono`, `celular`, `fechaNac`, `carrera`, `rol`, `estado`) VALUES
(1, 'admin@admin.com', 'administrador', 'administrador', 'admin',1, 1, 1, NULL, NULL, '2010-11-01', NULL, 'adm', 1);

-- -------------------------------------------------------------------------------------------------------------------
-- -------------------------------------------------------------------------------------------------------------------
-- Filtros para las tablas descargadas (dump)
-- -------------------------------------------------------------------------------------------------------------------

--
-- Filtros para la tabla `album`
--
ALTER TABLE `album`
  ADD CONSTRAINT `album_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `comentario`
--
ALTER TABLE `comentario`
  ADD CONSTRAINT `comentario_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `foto`
--
ALTER TABLE `foto`
  ADD CONSTRAINT `foto_ibfk_1` FOREIGN KEY (`idAlbum`) REFERENCES `album` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario`
--

ALTER TABLE `usuario` ADD FOREIGN KEY ( `fotoPerfil` ) REFERENCES `umbook`.`foto` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;

--
-- Filtros para la tabla `foto_comentario`
--
ALTER TABLE `foto_comentario`
  ADD CONSTRAINT `foto_comentario_ibfk_1` FOREIGN KEY (`idFoto`) REFERENCES `foto` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `foto_comentario_ibfk_2` FOREIGN KEY (`idComentario`) REFERENCES `comentario` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD CONSTRAINT `grupo_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `grupo_usuario`
--
ALTER TABLE `grupo_usuario`
  ADD CONSTRAINT `grupo_usuario_ibfk_1` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `grupo_usuario_ibfk_2` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `localidad`
--
ALTER TABLE `localidad`
  ADD CONSTRAINT `localidad_ibfk_1` FOREIGN KEY (`idCiudad`) REFERENCES `ciudad` (`id`);

--
-- Filtros para la tabla `muro`
--
ALTER TABLE `muro`
  ADD CONSTRAINT `muro_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `muro_ibfk_2` FOREIGN KEY (`idComentario`) REFERENCES `comentario` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `permiso_album`
--
ALTER TABLE `permiso_album`
  ADD CONSTRAINT `permiso_album_ibfk_1` FOREIGN KEY (`idAlbum`) REFERENCES `album` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permiso_album_ibfk_2` FOREIGN KEY (`idPermiso`) REFERENCES `permiso` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permiso_album_ibfk_3` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `permiso_muro`
--
ALTER TABLE `permiso_muro`
  ADD CONSTRAINT `permiso_muro_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permiso_muro_ibfk_2` FOREIGN KEY (`idPermiso`) REFERENCES `permiso` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `permiso_muro_ibfk_3` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
