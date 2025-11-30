package com.milsabores.ventas.entity;

/**
 * Enum que representa los métodos de pago disponibles
 */
public enum PaymentMethod {
    /**
     * Pago con tarjeta a través de Webpay
     */
    WEBPAY,

    /**
     * Transferencia bancaria
     */
    TRANSFERENCIA,

    /**
     * Pago en efectivo (retiro en tienda)
     */
    EFECTIVO
}
