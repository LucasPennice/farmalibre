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
('Paracetamol', 'Acetaminofén 500mg', 'Comprimido', 1),
('Ibuprofeno', 'Ibuprofeno 400mg', 'Comprimido', 1),
('Amoxicilina', 'Amoxicilina 500mg', 'Cápsula', 2),
('Testosterona', 'Testosterona enantato', 'Ampolla', 3),
('Diazepam', 'Diazepam 10mg', 'Comprimido', 4),
('Nandrolona', 'Nandrolona decanoato', 'Ampolla', 5);

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
(1, 1, 500, 120.50),
(2, 1, 300, 180.00),
(3, 2, 200, 950.00),
(4, 2, 100, 5200.00),
(5, 3, 80, 4300.00),
(6, 1, 60, 6700.00);

-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuario (nombre_completo_responsable, nombreUsuario, passEncriptada, direccion, rol) VALUES
('Administrador General', 'admin', 'admin123', 'Oficina Central', 'ADMIN'),
('María González', 'maria', 'maria123', 'Av. Siempre Viva 742', 'USUARIO'),
('Carlos López', 'carlos', 'carlos123', 'Calle Falsa 123', 'USUARIO');