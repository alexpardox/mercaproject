-- =========================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS MERCADÍA
-- Para PostgreSQL / Supabase
-- =========================================

-- Crear la base de datos (solo si no usas Supabase)
-- CREATE DATABASE mercadia_db;
-- \c mercadia_db;

-- =========================================
-- EXTENSIONES
-- =========================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================================
-- TIPOS ENUMERADOS
-- =========================================

-- Tipo para roles de usuario
CREATE TYPE usuario_rol AS ENUM (
    'ADMINISTRADOR',
    'COMERCIAL', 
    'TIENDA'
);

-- Tipo para estados de proveedor
CREATE TYPE proveedor_estado AS ENUM (
    'ACTIVO',
    'INACTIVO',
    'SUSPENDIDO'
);

-- Tipo para estados de formulario
CREATE TYPE formulario_estado AS ENUM (
    'PENDIENTE_APROBACION',
    'ACTIVO',
    'VENCIDO',
    'CANCELADO'
);

-- Tipo para tipos de espacio
CREATE TYPE tipo_espacio AS ENUM (
    'GONDOLA',
    'EXHIBIDOR',
    'ISLA',
    'ENTRADA',
    'CAJA',
    'PASILLO',
    'REFRIGERADOR',
    'CONGELADOR',
    'OTRO'
);

-- =========================================
-- TABLA: USUARIOS
-- =========================================
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    rol usuario_rol NOT NULL,
    tienda_asignada VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_acceso TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para usuarios
CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);

-- =========================================
-- TABLA: PROVEEDORES
-- =========================================
CREATE TABLE proveedores (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    rfc VARCHAR(13) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    telefono VARCHAR(10),
    direccion VARCHAR(500),
    contacto_principal VARCHAR(100) NOT NULL,
    estado proveedor_estado NOT NULL DEFAULT 'ACTIVO',
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para proveedores
CREATE INDEX idx_proveedores_rfc ON proveedores(rfc);
CREATE INDEX idx_proveedores_email ON proveedores(email);
CREATE INDEX idx_proveedores_estado ON proveedores(estado);
CREATE INDEX idx_proveedores_nombre ON proveedores(nombre);

-- =========================================
-- TABLA: FORMULARIOS
-- =========================================
CREATE TABLE formularios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    proveedor_id BIGINT NOT NULL,
    codigo_tienda VARCHAR(20) NOT NULL,
    nombre_tienda VARCHAR(100) NOT NULL,
    tipo_espacio tipo_espacio NOT NULL,
    area_asignada VARCHAR(100) NOT NULL,
    metros_cuadrados DECIMAL(8,2),
    numero_productos INTEGER,
    precio_acordado DECIMAL(12,2),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado formulario_estado NOT NULL DEFAULT 'PENDIENTE_APROBACION',
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Claves foráneas
    CONSTRAINT fk_formulario_usuario 
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    CONSTRAINT fk_formulario_proveedor 
        FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE RESTRICT,
        
    -- Validaciones
    CONSTRAINT chk_fecha_fin_mayor_inicio 
        CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT chk_metros_cuadrados_positivo 
        CHECK (metros_cuadrados > 0),
    CONSTRAINT chk_numero_productos_positivo 
        CHECK (numero_productos > 0),
    CONSTRAINT chk_precio_acordado_positivo 
        CHECK (precio_acordado > 0)
);

-- Índices para formularios
CREATE INDEX idx_formularios_usuario_id ON formularios(usuario_id);
CREATE INDEX idx_formularios_proveedor_id ON formularios(proveedor_id);
CREATE INDEX idx_formularios_codigo_tienda ON formularios(codigo_tienda);
CREATE INDEX idx_formularios_estado ON formularios(estado);
CREATE INDEX idx_formularios_fecha_inicio ON formularios(fecha_inicio);
CREATE INDEX idx_formularios_fecha_fin ON formularios(fecha_fin);
CREATE INDEX idx_formularios_tipo_espacio ON formularios(tipo_espacio);

-- =========================================
-- TRIGGERS PARA UPDATED_AT
-- =========================================

-- Función para actualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para cada tabla
CREATE TRIGGER update_usuarios_updated_at 
    BEFORE UPDATE ON usuarios 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_proveedores_updated_at 
    BEFORE UPDATE ON proveedores 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_formularios_updated_at 
    BEFORE UPDATE ON formularios 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =========================================
-- DATOS DE PRUEBA
-- =========================================

-- Usuarios de prueba (las contraseñas están hasheadas con BCrypt)
INSERT INTO usuarios (username, email, password, nombre_completo, rol, activo) VALUES
('admin', 'admin@mercadia.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye9DaKyYJ5Pd3Q6XL1BHGa.RJ4K8l1pS6', 'Administrador del Sistema', 'ADMINISTRADOR', true),
('comercial', 'comercial@mercadia.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye9DaKyYJ5Pd3Q6XL1BHGa.RJ4K8l1pS6', 'Usuario Área Comercial', 'COMERCIAL', true),
('tienda001', 'tienda001@mercadia.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye9DaKyYJ5Pd3Q6XL1BHGa.RJ4K8l1pS6', 'Usuario Tienda 001', 'TIENDA', true);

-- Actualizar tienda asignada para usuario de tienda
UPDATE usuarios SET tienda_asignada = 'T001' WHERE username = 'tienda001';

-- Proveedores de prueba
INSERT INTO proveedores (nombre, razon_social, rfc, email, telefono, contacto_principal, estado) VALUES
('Coca-Cola FEMSA', 'Coca-Cola FEMSA, S.A.B. de C.V.', 'CFE950101ABC', 'contacto@cocacolafemsa.com', '5551234567', 'Juan Pérez García', 'ACTIVO'),
('Grupo Bimbo', 'Grupo Bimbo, S.A.B. de C.V.', 'GBI980201DEF', 'contacto@grupobimbo.com', '5557890123', 'María López Rodríguez', 'ACTIVO'),
('Nestlé México', 'Nestlé México, S.A. de C.V.', 'NES850301GHI', 'contacto@nestle.com.mx', '5554567890', 'Carlos Hernández Díaz', 'ACTIVO'),
('Procter & Gamble', 'Procter & Gamble México, S. de R.L. de C.V.', 'PGM920401JKL', 'contacto@pg.com', '5556781234', 'Ana García Martínez', 'ACTIVO');

-- Formularios de ejemplo
INSERT INTO formularios (usuario_id, proveedor_id, codigo_tienda, nombre_tienda, tipo_espacio, area_asignada, metros_cuadrados, numero_productos, precio_acordado, fecha_inicio, fecha_fin, estado, observaciones) VALUES
(2, 1, 'T001', 'Tienda Centro Histórico', 'GONDOLA', 'Pasillo de Bebidas', 12.50, 25, 15000.00, '2025-08-01', '2025-12-31', 'ACTIVO', 'Espacio principal para productos Coca-Cola'),
(2, 2, 'T002', 'Tienda Norte', 'EXHIBIDOR', 'Área de Panadería', 8.00, 40, 12000.00, '2025-08-15', '2025-11-15', 'PENDIENTE_APROBACION', 'Exhibidor especial para productos Bimbo'),
(3, 3, 'T001', 'Tienda Centro Histórico', 'REFRIGERADOR', 'Sección Lácteos', 6.25, 15, 8500.00, '2025-09-01', '2025-12-31', 'ACTIVO', 'Refrigerador para productos Nestlé');

-- =========================================
-- VISTAS ÚTILES
-- =========================================

-- Vista de formularios con información completa
CREATE VIEW vista_formularios_completa AS
SELECT 
    f.id,
    f.codigo_tienda,
    f.nombre_tienda,
    f.tipo_espacio,
    f.area_asignada,
    f.metros_cuadrados,
    f.numero_productos,
    f.precio_acordado,
    f.fecha_inicio,
    f.fecha_fin,
    f.estado,
    f.observaciones,
    f.fecha_creacion,
    u.username as usuario_creador,
    u.nombre_completo as nombre_usuario,
    p.nombre as nombre_proveedor,
    p.rfc as rfc_proveedor,
    p.contacto_principal
FROM formularios f
JOIN usuarios u ON f.usuario_id = u.id
JOIN proveedores p ON f.proveedor_id = p.id;

-- Vista de estadísticas por tienda
CREATE VIEW vista_estadisticas_tienda AS
SELECT 
    codigo_tienda,
    nombre_tienda,
    COUNT(*) as total_formularios,
    COUNT(CASE WHEN estado = 'ACTIVO' THEN 1 END) as formularios_activos,
    COUNT(CASE WHEN estado = 'PENDIENTE_APROBACION' THEN 1 END) as formularios_pendientes,
    SUM(CASE WHEN estado = 'ACTIVO' THEN precio_acordado ELSE 0 END) as ingresos_activos,
    SUM(CASE WHEN estado = 'ACTIVO' THEN metros_cuadrados ELSE 0 END) as metros_utilizados
FROM formularios
GROUP BY codigo_tienda, nombre_tienda;

-- =========================================
-- FUNCIONES ÚTILES
-- =========================================

-- Función para obtener formularios próximos a vencer
CREATE OR REPLACE FUNCTION obtener_formularios_proximos_vencer(dias_anticipacion INTEGER DEFAULT 30)
RETURNS TABLE (
    id BIGINT,
    codigo_tienda VARCHAR(20),
    nombre_proveedor VARCHAR(100),
    fecha_fin DATE,
    dias_restantes INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id,
        f.codigo_tienda,
        p.nombre,
        f.fecha_fin,
        (f.fecha_fin - CURRENT_DATE)::INTEGER as dias_restantes
    FROM formularios f
    JOIN proveedores p ON f.proveedor_id = p.id
    WHERE f.estado = 'ACTIVO'
    AND f.fecha_fin BETWEEN CURRENT_DATE AND (CURRENT_DATE + dias_anticipacion)
    ORDER BY f.fecha_fin ASC;
END;
$$ LANGUAGE plpgsql;

-- =========================================
-- COMENTARIOS EN TABLAS
-- =========================================

COMMENT ON TABLE usuarios IS 'Tabla de usuarios del sistema Mercadía';
COMMENT ON TABLE proveedores IS 'Tabla de proveedores registrados en el sistema';
COMMENT ON TABLE formularios IS 'Tabla de formularios de asignación de espacios comerciales';

-- =========================================
-- PERMISOS Y SEGURIDAD (para Supabase)
-- =========================================

-- Habilitar RLS (Row Level Security) para Supabase
ALTER TABLE usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE proveedores ENABLE ROW LEVEL SECURITY;
ALTER TABLE formularios ENABLE ROW LEVEL SECURITY;

-- Políticas básicas (personaliza según tus necesidades)
-- Los usuarios pueden ver sus propios datos
CREATE POLICY "Usuarios pueden ver sus propios datos" ON usuarios
    FOR SELECT USING (auth.uid()::text = id::text);

-- Los administradores pueden ver todos los datos
CREATE POLICY "Administradores pueden ver todos los usuarios" ON usuarios
    FOR SELECT USING (
        EXISTS (
            SELECT 1 FROM usuarios 
            WHERE id::text = auth.uid()::text 
            AND rol = 'ADMINISTRADOR'
        )
    );

-- Política similar para proveedores y formularios
CREATE POLICY "Todos pueden ver proveedores" ON proveedores FOR SELECT USING (true);
CREATE POLICY "Todos pueden ver formularios" ON formularios FOR SELECT USING (true);

-- =========================================
-- SCRIPT COMPLETADO
-- =========================================

-- Verificar que todo se creó correctamente
DO $$
BEGIN
    RAISE NOTICE 'Base de datos Mercadía creada exitosamente';
    RAISE NOTICE 'Usuarios creados: %', (SELECT COUNT(*) FROM usuarios);
    RAISE NOTICE 'Proveedores creados: %', (SELECT COUNT(*) FROM proveedores);
    RAISE NOTICE 'Formularios creados: %', (SELECT COUNT(*) FROM formularios);
END $$;
