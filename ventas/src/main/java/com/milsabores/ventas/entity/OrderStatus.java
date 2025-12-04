package com.milsabores.ventas.entity;

/**
 * Enum que representa los posibles estados de una orden
 */
public enum OrderStatus {
    /**
     * Orden creada, pendiente de pago
     */
    PENDING,

    /**
     * Pago aprobado, orden confirmada
     */
    APPROVED,

    /**
     * Pago rechazado
     */
    REJECTED,

    /**
     * Orden cancelada por el usuario o sistema
     */
    CANCELLED,

    /**
     * Orden en proceso de preparación
     */
    PROCESSING,

    /**
     * Orden lista para entrega/retiro
     */
    READY,

    /**
     * Orden entregada
     */
    DELIVERED
}
