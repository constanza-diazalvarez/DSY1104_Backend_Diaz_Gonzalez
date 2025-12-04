package com.milsabores.ventas.exception;

/**
 * Excepción lanzada cuando no se encuentra una orden
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
