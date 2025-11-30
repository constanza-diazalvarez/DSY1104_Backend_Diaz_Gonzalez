package com.milsabores.ventas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad que representa un item dentro de una orden
 * Corresponde a un producto en el carrito del frontend
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código del producto (ej: TC001, TT002)
     */
    @Column(name = "product_code", nullable = false, length = 20)
    private String productCode;

    /**
     * Nombre del producto
     */
    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    /**
     * Precio unitario en CLP
     */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Cantidad de unidades
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Subtotal (precio * cantidad)
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    /**
     * Tamaño seleccionado (8 porciones, 12 porciones, 20 porciones)
     */
    @Column(name = "size_option", length = 50)
    private String sizeOption;

    /**
     * Mensaje personalizado para la torta
     */
    @Column(name = "custom_message", length = 100)
    private String customMessage;

    /**
     * Orden a la que pertenece este item
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Calcula el subtotal antes de persistir
     */
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
