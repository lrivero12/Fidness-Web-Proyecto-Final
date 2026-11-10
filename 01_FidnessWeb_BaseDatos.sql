-- FIDNESS WEB - Base de datos completa (estructura y datos)
-- Consolidado a partir de los 8 archivos SQL proporcionados.
-- ATENCION: este script reemplaza las tablas existentes.
CREATE DATABASE IF NOT EXISTS `fidness_web` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `fidness_web`;

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS;
SET FOREIGN_KEY_CHECKS=0;
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS;
SET UNIQUE_CHECKS=0;
SET NAMES utf8mb4;

-- ===== ROLES =====
--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `descripcion` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMINISTRADOR','Gestion general del sistema'),(2,'ENTRENADOR','Gestion de rutinas y ejercicios'),(3,'CLIENTE','Consulta de rutinas y membresias');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== USUARIOS =====
--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '1',
  `id_rol` int NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Administrador','Fidness','admin@fidness.com','$2a$10$iX8Yp0LneW7MVvu7Q2R.veElxe2J5GbOt49EQYRFjc8nNyRoFz09S','88888888',NULL,1,1,'2026-11-09 08:46:11'),(2,'Carlos Alberto','Rodríguez López','carlos.prueba@fidness.com','$2a$10$GnuX9iUIc1buPDdAm3zB/uqaOJ2MbWTEpe8hrhihpNV54slfj9NoG','88881122','2000-06-15',1,3,'2026-11-09 18:44:47'),(3,'Andrés Felipe','Martínez Vargas','andres.prueba@fidness.com','$2a$10$jLL2UBZ0sLi8ekUO/OECNe8KWfc20AvvUIKbReV3kEJ0g3PD0rh5i','87779900','1995-03-12',1,2,'2026-11-09 19:41:45'),(4,'Luis','Rivero Fallas','luis@prueba.com','$2a$10$5EvOUVpJ9N/L4T.P5k5COu/axiPjvRZouBUxNy7EUUXh7yy3.oiWy','12345678','2002-03-12',1,3,'2026-11-10 00:18:12');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== CLIENTES =====
--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,2),(2,4);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== ENTRENADORES =====
--
-- Table structure for table `entrenadores`
--

DROP TABLE IF EXISTS `entrenadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrenadores` (
  `id_entrenador` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_entrenador`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `fk_entrenador_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrenadores`
--

LOCK TABLES `entrenadores` WRITE;
/*!40000 ALTER TABLE `entrenadores` DISABLE KEYS */;
INSERT INTO `entrenadores` VALUES (1,3);
/*!40000 ALTER TABLE `entrenadores` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== CATEGORIAS =====
--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `series_recomendadas` int DEFAULT NULL,
  `repeticiones_recomendadas` varchar(30) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Pecho','Ejercicios enfocados en el desarrollo de los músculos pectorales.',4,'10-12',1),(2,'Piernas','Ejercicios para fortalecer cuádriceps, femorales y glúteos.',4,'10-12',1),(3,'Espalda','Ejercicios para desarrollar la musculatura de la espalda.',4,'12-15',1),(4,'Brazos','Ejercicios para fortalecer bíceps y tríceps.',3,'12-15',1),(5,'Cardio','Ejercicios aeróbicos para mejorar la resistencia cardiovascular.',3,'20-30 min',1),(6,'Hombros','Ejercicios para desarrollar los deltoides anteriores, laterales y posteriores.',3,'12-15',1);
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== EJERCICIOS =====
--
-- Table structure for table `ejercicios`
--

DROP TABLE IF EXISTS `ejercicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ejercicios` (
  `id_ejercicio` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `instrucciones` text,
  `nivel` varchar(30) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `id_categoria` int NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_ejercicio`),
  KEY `fk_ejercicio_categoria` (`id_categoria`),
  CONSTRAINT `fk_ejercicio_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejercicios`
--

LOCK TABLES `ejercicios` WRITE;
/*!40000 ALTER TABLE `ejercicios` DISABLE KEYS */;
INSERT INTO `ejercicios` VALUES (1,'Press de banca','Ejercicio de fuerza para desarrollar el pecho.','Acuéstate sobre el banco, baja la barra de forma controlada y empuja hasta extender los brazos.','Intermedio','press_banca.jpg',1,1),(2,'Sentadilla','Ejercicio compuesto para desarrollar piernas y glúteos.','Coloca los pies a la anchura de los hombros, flexiona las rodillas y vuelve a la posición inicial.','Intermedio','sentadilla.jpg',2,1),(3,'Dominadas','Ejercicio de tracción para fortalecer la espalda.','Sujeta la barra, eleva el cuerpo de forma controlada y desciende lentamente.','Avanzado','dominadas.jpg',3,1),(4,'Curl de bíceps','Ejercicio de aislamiento para fortalecer los bíceps.','Flexiona los codos manteniendo los brazos cerca del cuerpo y controla el descenso.','Principiante','curl_biceps.jpg',4,1),(5,'Burpees','Ejercicio cardiovascular de cuerpo completo.','Realiza una sentadilla, lleva los pies hacia atrás, vuelve a incorporarte y termina con un salto.','Principiante','burpees.jpg',5,1),(6,'Press inclinado','Ejercicio para desarrollar la región superior del pecho.','Ajusta el banco en posición inclinada y realiza el movimiento de empuje de manera controlada.','Intermedio','press_inclinado.jpg',1,1),(7,'Press militar con mancuernas','Ejercicio de fuerza para desarrollar los deltoides mediante un movimiento de empuje vertical.','Sentarse con la espalda apoyada, sostener las mancuernas a la altura de los hombros y extender los brazos hacia arriba de forma controlada.','Intermedio','press_militar.jpg',6,1);
/*!40000 ALTER TABLE `ejercicios` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== CLASES =====
--
-- Table structure for table `clases`
--

DROP TABLE IF EXISTS `clases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clases` (
  `id_clase` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `fecha` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `duracion_minutos` int NOT NULL,
  `capacidad_maxima` int NOT NULL,
  `id_entrenador` int NOT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'PROGRAMADA',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_clase`),
  KEY `fk_clase_entrenador` (`id_entrenador`),
  CONSTRAINT `fk_clase_entrenador` FOREIGN KEY (`id_entrenador`) REFERENCES `entrenadores` (`id_entrenador`),
  CONSTRAINT `chk_clase_capacidad` CHECK ((`capacidad_maxima` > 0)),
  CONSTRAINT `chk_clase_duracion` CHECK ((`duracion_minutos` > 0)),
  CONSTRAINT `chk_clase_estado` CHECK ((`estado` in (_utf8mb4'PROGRAMADA',_utf8mb4'CANCELADA',_utf8mb4'FINALIZADA')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases`
--

LOCK TABLES `clases` WRITE;
/*!40000 ALTER TABLE `clases` DISABLE KEYS */;
INSERT INTO `clases` VALUES (1,'Entrenamiento funcional avanzado','Sesión de entrenamiento de fuerza, resistencia y acondicionamiento físico.','2026-11-18','10:00:00',75,20,1,'CANCELADA','2026-11-09 21:44:05'),(2,'Entrenamiento de fuerza','Sesión de entrenamiento de fuerza para todos los niveles.','2026-11-20','09:00:00',60,10,1,'PROGRAMADA','2026-11-09 22:33:51'),(3,'Entreno Pierna','Ejercicios de Pierna','2026-11-14','10:00:00',70,25,1,'PROGRAMADA','2026-11-09 23:02:07');
/*!40000 ALTER TABLE `clases` ENABLE KEYS */;
UNLOCK TABLES;

-- ===== RESERVAS =====
--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `id_reserva` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `id_clase` int NOT NULL,
  `fecha_reserva` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` varchar(20) NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_reserva`),
  KEY `fk_reserva_cliente` (`id_cliente`),
  KEY `fk_reserva_clase` (`id_clase`),
  CONSTRAINT `fk_reserva_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`),
  CONSTRAINT `fk_reserva_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `chk_reserva_estado` CHECK ((`estado` in (_utf8mb4'PENDIENTE',_utf8mb4'CONFIRMADA',_utf8mb4'RECHAZADA',_utf8mb4'CANCELADA')))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

LOCK TABLES `reservas` WRITE;
/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (1,1,2,'2026-11-09 22:35:28','CONFIRMADA'),(2,1,3,'2026-11-09 23:02:42','RECHAZADA');
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
