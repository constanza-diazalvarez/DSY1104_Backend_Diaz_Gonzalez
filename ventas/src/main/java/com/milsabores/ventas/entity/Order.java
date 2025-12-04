package com.milsabores.ventas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una orden de compra
 * Mapea la tabla 'orders' en la base de datos PostgreSQL
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número único de orden (formato: ORD-{timestamp})
     */
    @Column(name = "buy_order", nullable = false, unique = true, length = 50)
    private String buyOrder;

    /**
     * Estado de la orden: PENDING, APPROVED, REJECTED, CANCELLED
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    /**
     * Monto total de la orden en CLP
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    /**
     * Monto de descuento aplicado
     */
    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    /**
     * Monto final después de descuentos
     */
    @Column(name = "final_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount;

    /**
     * Código de autorización del pago (si fue aprobado)
     */
    @Column(name = "authorization_code", length = 20)
    private String authorizationCode;

    /**
     * Últimos 4 dígitos de la tarjeta usada
     */
    @Column(name = "card_number", length = 4)
    private String cardNumber;

    /**
     * Método de pago: WEBPAY, TRANSFERENCIA, EFECTIVO
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    /**
     * Mensaje de error si el pago falló
     */
    @Column(name = "error_message", length = 255)
    private String errorMessage;

    /**
     * Fecha de creación de la orden
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Fecha de la transacción de pago
     */
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    /**
     * Información del cliente (embebida)
     */
    @Embedded
    private CustomerInfo customerInfo;

    /**
     * Items de la orden (relación uno a muchos)
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Método helper para agregar un item a la orden
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Método helper para remover un item de la orden
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
