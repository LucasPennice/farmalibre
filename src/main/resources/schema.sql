CREATE DATABASE IF NOT EXISTS farmacia_db;
USE farmacia_db;

-- =========================
-- CATEGORIA DROGA
-- =========================
CREATE TABLE IF NOT EXISTS categoria_droga (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    aprobacion_pendiente BOOLEAN NOT NULL DEFAULT FALSE
);

-- =========================
-- DROGA
-- =========================
CREATE TABLE IF NOT EXISTS droga (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    composicion TEXT NOT NULL,
    unidad VARCHAR(50) NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria_droga(id)
);

-- =========================
-- PROVEEDOR
-- =========================
CREATE TABLE IF NOT EXISTS proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(150) NOT NULL,
    nombre_fantasia VARCHAR(150),
    cuit VARCHAR(20) NOT NULL UNIQUE,
    tipo_persona ENUM('FISICA', 'JURIDICA') NOT NULL
);

-- =========================
-- STOCK PROVEEDOR DROGA
-- =========================
CREATE TABLE IF NOT EXISTS stock_droga_proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    droga_id INT NOT NULL,
    proveedor_id INT NOT NULL,
    disponible INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (droga_id) REFERENCES droga(id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id),
    UNIQUE (droga_id, proveedor_id)
);

-- =========================
-- USUARIO
-- =========================
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo_responsable VARCHAR(150) NOT NULL,
    direccion VARCHAR(200),
    foto_perfil MEDIUMBLOB,
    rol ENUM('USUARIO', 'ADMIN') NOT NULL
);
