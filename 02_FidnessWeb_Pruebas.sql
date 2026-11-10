
CREATE DATABASE IF NOT EXISTS fidness_web
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE fidness_web;

SELECT DATABASE();

USE fidness_web;

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(150)
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    id_rol INT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES roles(id_rol)
);

-- Registrar los tres roles del sistema
INSERT IGNORE INTO roles (nombre, descripcion)
VALUES
('ADMINISTRADOR', 'Gestion general del sistema'),
('ENTRENADOR', 'Gestion de rutinas y ejercicios'),
('CLIENTE', 'Consulta de rutinas y membresias');

-- Verificar las tablas y los roles
SHOW TABLES;

SELECT * FROM roles;


USE fidness_web;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,

    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
);

-- Tabla de entrenadores
CREATE TABLE IF NOT EXISTS entrenadores (
    id_entrenador INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,

    CONSTRAINT fk_entrenador_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
);

-- Verificar las tablas creadas
SHOW TABLES;


USE fidness_web;

-- Tabla de categorías de ejercicios
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    series_recomendadas INT,
    repeticiones_recomendadas VARCHAR(30),
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de ejercicios
CREATE TABLE IF NOT EXISTS ejercicios (
    id_ejercicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    instrucciones TEXT,
    nivel VARCHAR(30) NOT NULL,
    imagen VARCHAR(255),
    id_categoria INT NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_ejercicio_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categorias(id_categoria)
);

-- Verificar las tablas
SHOW TABLES;


USE fidness_web;

INSERT INTO categorias
(nombre, descripcion, series_recomendadas, repeticiones_recomendadas)
VALUES
('Pecho', 'Ejercicios enfocados en el desarrollo de los músculos pectorales.', 4, '10-12'),
('Piernas', 'Ejercicios para fortalecer cuádriceps, femorales y glúteos.', 4, '10-12'),
('Espalda', 'Ejercicios para desarrollar la musculatura de la espalda.', 4, '12-15'),
('Brazos', 'Ejercicios para fortalecer bíceps y tríceps.', 3, '12-15'),
('Cardio', 'Ejercicios aeróbicos para mejorar la resistencia cardiovascular.', 3, '20-30 min');

-- Verificar los registros
SELECT * FROM categorias;


USE fidness_web;

INSERT INTO ejercicios
(nombre, descripcion, instrucciones, nivel, imagen, id_categoria)
VALUES
('Press de banca',
 'Ejercicio de fuerza para desarrollar el pecho.',
 'Acuéstate sobre el banco, baja la barra de forma controlada y empuja hasta extender los brazos.',
 'Intermedio', 'press_banca.jpg', 1),

('Sentadilla',
 'Ejercicio compuesto para desarrollar piernas y glúteos.',
 'Coloca los pies a la anchura de los hombros, flexiona las rodillas y vuelve a la posición inicial.',
 'Intermedio', 'sentadilla.jpg', 2),

('Dominadas',
 'Ejercicio de tracción para fortalecer la espalda.',
 'Sujeta la barra, eleva el cuerpo de forma controlada y desciende lentamente.',
 'Avanzado', 'dominadas.jpg', 3),

('Curl de bíceps',
 'Ejercicio de aislamiento para fortalecer los bíceps.',
 'Flexiona los codos manteniendo los brazos cerca del cuerpo y controla el descenso.',
 'Principiante', 'curl_biceps.jpg', 4),

('Burpees',
 'Ejercicio cardiovascular de cuerpo completo.',
 'Realiza una sentadilla, lleva los pies hacia atrás, vuelve a incorporarte y termina con un salto.',
 'Principiante', 'burpees.jpg', 5),

('Press inclinado',
 'Ejercicio para desarrollar la región superior del pecho.',
 'Ajusta el banco en posición inclinada y realiza el movimiento de empuje de manera controlada.',
 'Intermedio', 'press_inclinado.jpg', 1);
 
-- Verificar los ejercicios y sus categorías
SELECT
    e.id_ejercicio,
    e.nombre AS ejercicio,
    c.nombre AS categoria,
    e.nivel,
    e.estado
FROM ejercicios e
INNER JOIN categorias c
    ON e.id_categoria = c.id_categoria
ORDER BY e.id_ejercicio;

USE fidness_web;

SHOW TABLES;


USE fidness_web;

INSERT INTO usuarios (
    nombre,
    apellidos,
    correo,
    contrasena,
    telefono,
    estado,
    id_rol
)
SELECT
    'Administrador',
    'Fidness',
    'admin@fidness.com',
    '$2a$10$baYo9VDm9g/iR4q7cvoJwup85PW.B9UuU8IK8v5FVrMLNI6LALDgC',
    '88888888',
    TRUE,
    id_rol
FROM roles
WHERE nombre = 'ADMINISTRADOR';


USE fidness_web;




SELECT
    u.id_usuario,
    u.nombre,
    u.apellidos,
    u.correo,
    u.estado,
    r.nombre AS rol
FROM usuarios u
INNER JOIN roles r ON u.id_rol = r.id_rol
WHERE u.correo = 'admin@fidness.com';




USE fidness_web;

UPDATE usuarios
SET contrasena = '$2a$10$wW/05R2cl6pOwlueJiQwMQQnWpQ9HpbkKAaOKxkFQ8XsdJGTPiAC'
WHERE correo = 'admin@fidness.com';



SELECT
    id_usuario,
    nombre,
    correo,
    estado,
    LENGTH(contrasena) AS longitud_hash
FROM usuarios
WHERE correo = 'admin@fidness.com';


USE fidness_web;

UPDATE usuarios
SET contrasena = '$2a$10$iX8YpOLnewM7MVvu7Q2R.veElxe2J5GbOt49EQYRFjc8nNyRoFz09S'
WHERE correo = 'admin@fidness.com';


SELECT
    id_usuario,
    nombre,
    correo,
    estado,
    LENGTH(contrasena) AS longitud_hash
FROM usuarios
WHERE correo = 'admin@fidness.com';


USE fidness_web;

SELECT
    LENGTH(contrasena) AS longitud,
    LEFT(contrasena, 7) AS inicio_hash,
    RIGHT(contrasena, 10) AS final_hash,
    HEX(RIGHT(contrasena, 3)) AS ultimos_caracteres_hex
FROM usuarios
WHERE correo = 'admin@fidness.com';


USE fidness_web;

UPDATE usuarios
SET contrasena = '$2a$10$iX8Yp0LneW7MVvu7Q2R.veElxe2J5GbOt49EQYRFjc8nNyRoFz09S'
WHERE correo = 'admin@fidness.com';

SELECT
    correo,
    LENGTH(contrasena) AS longitud_hash
FROM usuarios
WHERE correo = 'admin@fidness.com';


USE fidness_web;

SELECT
    id_categoria,
    nombre,
    descripcion,
    series_recomendadas,
    repeticiones_recomendadas,
    estado
FROM categorias
ORDER BY id_categoria;


USE fidness_web;

SELECT
    id_categoria,
    nombre,
    descripcion,
    series_recomendadas,
    repeticiones_recomendadas,
    estado
FROM categorias
WHERE nombre = 'Hombros';

USE fidness_web;

SELECT
    id_categoria,
    nombre
FROM categorias
WHERE LOWER(nombre) = 'pecho';


USE fidness_web;

SELECT
    id_categoria,
    nombre,
    estado
FROM categorias
WHERE id_categoria = 6;


USE fidness_web;

SELECT
    id_categoria,
    nombre,
    estado
FROM categorias
WHERE id_categoria = 6;

USE fidness_web;

SELECT
    e.id_ejercicio,
    e.nombre,
    c.nombre AS categoria,
    e.nivel,
    e.estado
FROM ejercicios e
INNER JOIN categorias c
    ON e.id_categoria = c.id_categoria
WHERE e.nombre = 'Press militar';

USE fidness_web;

SELECT
    e.id_ejercicio,
    e.nombre,
    e.descripcion,
    c.nombre AS categoria,
    e.nivel,
    e.estado
FROM ejercicios e
INNER JOIN categorias c
    ON e.id_categoria = c.id_categoria
WHERE e.id_ejercicio = 7;


USE fidness_web;

SELECT id_ejercicio, nombre, estado
FROM ejercicios
WHERE id_ejercicio = 7;


USE fidness_web;

SELECT
    c.id_cliente,
    u.id_usuario,
    u.nombre,
    u.apellidos,
    u.correo,
    r.nombre AS rol,
    u.estado
FROM clientes c
INNER JOIN usuarios u
    ON c.id_usuario = u.id_usuario
INNER JOIN roles r
    ON u.id_rol = r.id_rol;
    
USE fidness_web;

SELECT
    c.id_cliente,
    u.id_usuario,
    u.nombre,
    u.apellidos,
    u.correo,
    u.telefono,
    u.estado
FROM clientes c
INNER JOIN usuarios u
    ON c.id_usuario = u.id_usuario;
    
USE fidness_web;

SELECT
    id_usuario,
    nombre,
    apellidos,
    correo,
    estado
FROM usuarios
WHERE id_usuario = 2;

USE fidness_web;

SELECT
    id_usuario,
    nombre,
    apellidos,
    correo,
    telefono,
    fecha_nacimiento,
    estado
FROM usuarios
WHERE id_usuario = 2;

USE fidness_web;

SELECT
    e.id_entrenador,
    u.id_usuario,
    u.nombre,
    u.apellidos,
    u.correo,
    u.telefono,
    r.nombre AS rol,
    u.estado
FROM entrenadores e
INNER JOIN usuarios u
    ON e.id_usuario = u.id_usuario
INNER JOIN roles r
    ON u.id_rol = r.id_rol;

USE fidness_web;

SELECT
    e.id_entrenador,
    u.id_usuario,
    u.nombre,
    u.apellidos,
    u.correo,
    u.telefono,
    r.nombre AS rol,
    u.estado
FROM entrenadores e
INNER JOIN usuarios u
    ON e.id_usuario = u.id_usuario
INNER JOIN roles r
    ON u.id_rol = r.id_rol
WHERE e.id_entrenador = 1;

USE fidness_web;

SELECT
    id_usuario,
    nombre,
    apellidos,
    estado
FROM usuarios
WHERE id_usuario = 3;

USE fidness_web;

SHOW TABLES;


USE fidness_web;

-- ==========================================
-- TABLA: CLASES
-- Proyecto Fidness Web
-- ==========================================

CREATE TABLE clases (

    id_clase INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,

    descripcion TEXT,

    fecha DATE NOT NULL,

    hora_inicio TIME NOT NULL,

    duracion_minutos INT NOT NULL,

    capacidad_maxima INT NOT NULL,

    id_entrenador INT NOT NULL,

    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',

    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_clase_entrenador
        FOREIGN KEY (id_entrenador)
        REFERENCES entrenadores(id_entrenador),

    CONSTRAINT chk_clase_duracion
        CHECK (duracion_minutos > 0),

    CONSTRAINT chk_clase_capacidad
        CHECK (capacidad_maxima > 0),

    CONSTRAINT chk_clase_estado
        CHECK (estado IN (
            'PROGRAMADA',
            'CANCELADA',
            'FINALIZADA'
        ))

);

USE fidness_web;

DESCRIBE clases;

USE fidness_web;

SELECT
    c.id_clase,
    c.nombre,
    c.descripcion,
    c.fecha,
    c.hora_inicio,
    c.duracion_minutos,
    c.capacidad_maxima,
    u.nombre AS entrenador,
    c.estado
FROM clases c
INNER JOIN entrenadores e
    ON c.id_entrenador = e.id_entrenador
INNER JOIN usuarios u
    ON e.id_usuario = u.id_usuario
ORDER BY c.id_clase;

USE fidness_web;

SELECT
    id_clase,
    nombre,
    fecha,
    hora_inicio,
    duracion_minutos,
    capacidad_maxima,
    estado
FROM clases
WHERE id_clase = 1;


USE fidness_web;

SELECT
    id_clase,
    nombre,
    fecha,
    hora_inicio,
    capacidad_maxima,
    estado
FROM clases
WHERE id_clase = 1;


USE fidness_web;

-- ==========================================
-- TABLA: RESERVAS
-- Proyecto: Fidness Web
-- ==========================================

CREATE TABLE IF NOT EXISTS reservas (

    id_reserva INT AUTO_INCREMENT PRIMARY KEY,

    -- Cliente que realiza la reserva
    id_cliente INT NOT NULL,

    -- Clase que desea reservar
    id_clase INT NOT NULL,

    -- Fecha de creación de la reserva
    fecha_reserva TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    -- Estado de la reserva
    estado VARCHAR(20) NOT NULL
        DEFAULT 'PENDIENTE',

    -- Evitar reservas duplicadas activas
    -- mediante validaciones en Java
    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente),

    CONSTRAINT fk_reserva_clase
        FOREIGN KEY (id_clase)
        REFERENCES clases(id_clase),

    -- Estados permitidos
    CONSTRAINT chk_reserva_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'CONFIRMADA',
                'RECHAZADA',
                'CANCELADA'
            )
        )

);
SHOW TABLES;
DESCRIBE reservas;

USE fidness_web;

SELECT
    id_reserva,
    id_cliente,
    id_clase,
    fecha_reserva,
    estado
FROM reservas;



