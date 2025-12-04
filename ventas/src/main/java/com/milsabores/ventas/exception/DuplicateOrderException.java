package com.milsabores.ventas.exception;

/**
 * Excepción lanzada cuando se intenta crear una orden duplicada
 */
public class DuplicateOrderException extends RuntimeException {

    public DuplicateOrderException(String message) {
        super(message);
    }

    public DuplicateOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
