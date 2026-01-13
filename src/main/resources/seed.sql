USE farmacia_db;

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE stock_droga_proveedor;
TRUNCATE TABLE droga;
TRUNCATE TABLE categoria_droga;
SET FOREIGN_KEY_CHECKS=1;

-- =========================
-- CATEGORIAS
-- =========================
INSERT INTO categoria_droga (id, nombre, aprobacion_pendiente) VALUES
(1, 'Analgésicos', FALSE),
(2, 'Antibióticos', FALSE),
(3, 'Hormonas', FALSE),
(4, 'Psicotrópicos', TRUE),
(5, 'Esteroides', TRUE);

ALTER TABLE categoria_droga AUTO_INCREMENT = 6;

-- =========================
-- DROGAS
-- =========================
INSERT INTO droga (nombre, composicion, unidad, categoria_id) VALUES
('Paracetamol', 'C8H9NO2', 'Comprimido', 1),
('Ibuprofeno', 'C13H18O2', 'Comprimido', 1),
('Amoxicilina', 'C16H19N3O5S', 'Cápsula', 2),
('Testosterona', 'C19H28O2', 'Ampolla', 3),
('Diazepam', 'C16H13ClN2O', 'Comprimido', 4),
('Nandrolona', 'C18H26O2', 'Ampolla', 5);

-- =========================
-- PROVEEDORES
-- =========================
INSERT INTO proveedor (razon_social, nombre_fantasia, cuit, tipo_persona) VALUES
('Laboratorios Delta SA', 'Delta Pharma', '30-12345678-9', 'JURIDICA'),
('Farmacéutica Río', 'Río Salud', '30-98765432-1', 'JURIDICA'),
('Juan Pérez', NULL, '20-22334455-6', 'FISICA');

-- =========================
-- STOCK PROVEEDOR - DROGA
-- =========================
INSERT INTO stock_droga_proveedor (droga_id, proveedor_id, disponible, precio_unitario) VALUES
-- Paracetamol
(1, 1, 500, 120.50),
(1, 2, 200, 118.00),
(1, 3, 100, 125.00),

-- Ibuprofeno
(2, 1, 300, 180.00),
(2, 2, 150, 175.00),

-- Amoxicilina
(3, 1, 120, 980.00),
(3, 2, 200, 950.00),
(3, 3, 80, 1020.00),

-- Testosterona
(4, 1, 90, 5400.00),
(4, 2, 100, 5200.00),

-- Diazepam
(5, 2, 60, 4500.00),
(5, 3, 80, 4300.00),

-- Nandrolona
(6, 1, 60, 6700.00),
(6, 2, 40, 6600.00);

-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuario (nombre_completo_responsable, nombreUsuario, passEncriptada, direccion, rol) VALUES
('Administrador General', 'admin', 'admin123', 'Oficina Central', 'ADMIN'),
('María González', 'maria', 'maria123', 'Av. Siempre Viva 742', 'USUARIO'),
('Carlos López', 'carlos', 'carlos123', 'Calle Falsa 123', 'USUARIO');