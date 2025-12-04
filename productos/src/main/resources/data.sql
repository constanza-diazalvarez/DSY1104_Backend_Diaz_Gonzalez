-- Insertar productos en la tabla principal
INSERT INTO productos (code, nombre, categoria_id, tipo_forma, precioclp, stock, personalizable, max_msg_chars, descripcion, imagen) VALUES
('TC001', 'Torta Cuadrada de Chocolate', 'TC', 'cuadrada', 45000, 10, true, 50, 'Deliciosa torta de chocolate con ganache y toque de avellanas. Ideal para personalizar con mensaje.', '../img/productos/1.png'),
('TC002', 'Torta Cuadrada de Frutas', 'TC', 'cuadrada', 50000, 8, true, 50, 'Bizcocho de vainilla con frutas frescas y crema chantilly.', '../img/productos/2.png'),
('TT001', 'Torta Circular de Vainilla', 'TT', 'circular', 40000, 12, true, 50, 'Vainilla clásica rellena con crema pastelera y glaseado dulce.', '../img/productos/3.png'),
('TT002', 'Torta Circular de Manjar', 'TT', 'circular', 42000, 9, true, 50, 'Clásica torta chilena con manjar y nueces.', '../img/productos/4.png'),
('PI001', 'Mousse de Chocolate', 'PI', NULL, 5000, 40, false, 0, 'Postre cremoso con chocolate de alta calidad.', '../img/productos/5.png'),
('PI002', 'Tiramisú', 'PI', NULL, 5500, 36, false, 0, 'Café, mascarpone y cacao en un equilibrio perfecto.', '../img/productos/6.png'),
('PSA001', 'Torta de Naranja', 'PSA', 'circular', 48000, 7, true, 50, 'Preparación sin azúcar endulzada naturalmente. Ideal para quienes buscan opciones más saludables.', '../img/productos/7.png'),
('PSA002', 'Cheesecake', 'PSA', 'circular', 47000, 6, true, 50, 'Preparación sin azúcar endulzada naturalmente. Suave y cremoso, ideal para disfrutar sin culpa.', '../img/productos/8.png'),
('PT001', 'Empanada de Manzana', 'PT', NULL, 3000, 50, false, 0, 'Rellena de manzanas especiadas, perfecta para el desayuno o merienda.', '../img/productos/9.png'),
('PT002', 'Tarta de Santiago', 'PT', 'circular', 6000, 22, false, 0, 'Clásica tarta de almendras, azúcar y huevos.', '../img/productos/10.png'),
('PG001', 'Brownie', 'PG', 'cuadrada', 4000, 35, false, 0, 'Denso y sabroso, libre de gluten.', '../img/productos/11.png'),
('PG002', 'Pan', 'PG', NULL, 3500, 28, false, 0, 'Preparación sin gluten. Suave y esponjoso, ideal para sándwiches.', '../img/productos/12.png'),
('PV001', 'Torta de Chocolate', 'PV', 'circular', 50000, 6, true, 50, 'Húmeda y deliciosa. Preparación vegana sin ingredientes de origen animal.', '../img/productos/13.png'),
('PV002', 'Galletas de Avena', 'PV', NULL, 4500, 40, false, 0, 'Crujientes y sabrosas, perfectas para colación. Preparación vegana sin ingredientes de origen animal.', '../img/productos/14.png'),
('TE001', 'Torta de Cumpleaños', 'TE', 'circular', 55000, 7, true, 50, 'Pensada para celebrar: admite decoraciones temáticas y mensaje.', '../img/productos/15.png'),
('TE002', 'Torta de Boda', 'TE', 'circular', 60000, 4, true, 50, 'Elegante y memorable; lista para personalizar.', '../img/productos/16.png');

-- Insertar tamaños disponibles
INSERT INTO producto_tamanos_disponibles (producto_code, tamanos_disponibles) VALUES
('TC001', '8 porciones'), ('TC001', '12 porciones'), ('TC001', '20 porciones'),
('TC002', '8 porciones'), ('TC002', '12 porciones'), ('TC002', '20 porciones'),
('TT001', '8 porciones'), ('TT001', '12 porciones'), ('TT001', '20 porciones'),
('TT002', '8 porciones'), ('TT002', '12 porciones'), ('TT002', '20 porciones'),
('PI001', 'unidad'),
('PI002', 'unidad'),
('PSA001', '8 porciones'), ('PSA001', '12 porciones'),
('PSA002', '8 porciones'), ('PSA002', '12 porciones'),
('PT001', 'unidad'),
('PT002', '8 porciones'),
('PG001', 'unidad'),
('PG002', 'unidad'),
('PV001', '8 porciones'), ('PV001', '12 porciones'),
('PV002', 'unidad'),
('TE001', '8 porciones'), ('TE001', '12 porciones'), ('TE001', '20 porciones'),
('TE002', '12 porciones'), ('TE002', '20 porciones');

-- Insertar etiquetas
INSERT INTO producto_etiquetas (producto_code, etiquetas) VALUES
('TC001', 'tradicional'),
('TC002', 'tradicional'),
('TT001', 'tradicional'),
('TT002', 'tradicional'),
('PI001', 'clasico'),
('PI002', 'clasico'),
('PSA001', 'sin_azucar'),
('PSA002', 'sin_azucar'),
('PT001', 'tradicional'),
('PT002', 'tradicional'),
('PG001', 'sin_gluten'),
('PG002', 'sin_gluten'),
('PV001', 'vegana'),
('PV002', 'vegana'),
('TE001', 'especial'), ('TE001', 'cumpleaños'),
('TE002', 'especial'), ('TE002', 'boda');

-- Insertar sabores
INSERT INTO producto_sabor (producto_code, sabor) VALUES
('TC001', 'chocolate'),
('TC002', 'frutas'),
('TT001', 'vainilla'),
('TT002', 'manjar'),
('PI001', 'chocolate'),
('PI002', 'cafe'),
('PSA001', 'naranja'),
('PG001', 'chocolate'),
('PV001', 'chocolate'),
('PV002', 'avena'),
('PT001', 'manzana'),
('PT002', 'almendras');
