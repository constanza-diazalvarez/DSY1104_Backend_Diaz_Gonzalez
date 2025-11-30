-- =====================================================
-- Script de creación de base de datos para Ventas
-- Microservicio: ventas
-- Base de datos: PostgreSQL
-- =====================================================

-- Crear la base de datos (ejecutar como superusuario si es necesario)
-- CREATE DATABASE milsabores_ventas;

-- Conectar a la base de datos
-- \c milsabores_ventas

-- =====================================================
-- Tabla: orders (Órdenes de compra)
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    buy_order VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    amount DECIMAL(12, 2) NOT NULL,
    discount_amount DECIMAL(12, 2) DEFAULT 0,
    final_amount DECIMAL(12, 2) NOT NULL,
    authorization_code VARCHAR(20),
    card_number VARCHAR(4),
    payment_method VARCHAR(20) NOT NULL,
    error_message VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    transaction_date TIMESTAMP,
    
    -- Customer Info (embedded)
    customer_name VARCHAR(150) NOT NULL,
    customer_email VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    customer_address VARCHAR(255) NOT NULL,
    customer_comuna VARCHAR(100) NOT NULL,
    customer_city VARCHAR(100) NOT NULL,
    
    -- Constraints
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'PROCESSING', 'READY', 'DELIVERED')),
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('WEBPAY', 'TRANSFERENCIA', 'EFECTIVO')),
    CONSTRAINT chk_amount_positive CHECK (amount >= 0),
    CONSTRAINT chk_final_amount_positive CHECK (final_amount >= 0)
);

-- =====================================================
-- Tabla: order_items (Items de cada orden)
-- =====================================================
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    size_option VARCHAR(50),
    custom_message VARCHAR(100),
    
    -- Foreign Key
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) 
        REFERENCES orders(id) ON DELETE CASCADE,
    
    -- Constraints
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_unit_price_positive CHECK (unit_price > 0)
);

-- =====================================================
-- Índices para mejorar el rendimiento
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_orders_buy_order ON orders(buy_order);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_customer_email ON orders(customer_email);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product_code ON order_items(product_code);

-- =====================================================
-- Datos de prueba (opcional)
-- =====================================================

-- Orden de prueba 1: Aprobada
INSERT INTO orders (
    buy_order, status, amount, discount_amount, final_amount, 
    authorization_code, card_number, payment_method, 
    created_at, transaction_date,
    customer_name, customer_email, customer_phone, 
    customer_address, customer_comuna, customer_city
) VALUES (
    'ORD-1701234567890', 'APPROVED', 95000.00, 5000.00, 90000.00,
    '123456', '6623', 'WEBPAY',
    NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days',
    'Juan Pérez González', 'juan.perez@email.com', '+56 9 1234 5678',
    'Av. Providencia 1234, Depto 501', 'Providencia', 'Santiago'
);

-- Items de la orden 1
INSERT INTO order_items (
    order_id, product_code, product_name, unit_price, quantity, subtotal, size_option, custom_message
) VALUES 
    (1, 'TC001', 'Torta Cuadrada de Chocolate', 45000.00, 1, 45000.00, '12 porciones', 'Feliz Cumpleaños María!'),
    (1, 'TT001', 'Torta Circular de Vainilla', 50000.00, 1, 50000.00, '8 porciones', NULL);

-- Orden de prueba 2: Pendiente
INSERT INTO orders (
    buy_order, status, amount, discount_amount, final_amount,
    payment_method, created_at,
    customer_name, customer_email, customer_phone,
    customer_address, customer_comuna, customer_city
) VALUES (
    'ORD-1701234567891', 'PENDING', 45000.00, 0.00, 45000.00,
    'WEBPAY', NOW(),
    'María López Silva', 'maria.lopez@email.com', '+56 9 8765 4321',
    'Las Condes 5678', 'Las Condes', 'Santiago'
);

-- Items de la orden 2
INSERT INTO order_items (
    order_id, product_code, product_name, unit_price, quantity, subtotal, size_option
) VALUES 
    (2, 'TC002', 'Torta Cuadrada de Frutas', 45000.00, 1, 45000.00, '8 porciones');

-- =====================================================
-- Verificar datos
-- =====================================================
-- SELECT * FROM orders;
-- SELECT * FROM order_items;
-- SELECT o.buy_order, o.status, o.final_amount, COUNT(oi.id) as total_items
-- FROM orders o
-- LEFT JOIN order_items oi ON o.id = oi.order_id
-- GROUP BY o.id;
