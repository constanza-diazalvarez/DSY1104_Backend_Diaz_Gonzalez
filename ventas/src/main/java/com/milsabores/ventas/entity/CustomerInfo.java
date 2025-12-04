package com.milsabores.ventas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Clase embebida que contiene la información del cliente
 * Se almacena dentro de la tabla orders
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInfo {

    /**
     * Nombre completo del cliente
     */
    @Column(name = "customer_name", nullable = false, length = 150)
    private String nombre;

    /**
     * Email del cliente
     */
    @Column(name = "customer_email", nullable = false, length = 100)
    private String email;

    /**
     * Teléfono de contacto
     */
    @Column(name = "customer_phone", nullable = false, length = 20)
    private String telefono;

    /**
     * Dirección de entrega
     */
    @Column(name = "customer_address", nullable = false, length = 255)
    private String direccion;

    /**
     * Comuna de entrega
     */
    @Column(name = "customer_comuna", nullable = false, length = 100)
    private String comuna;

    /**
     * Ciudad de entrega
     */
    @Column(name = "customer_city", nullable = false, length = 100)
    private String ciudad;
}
