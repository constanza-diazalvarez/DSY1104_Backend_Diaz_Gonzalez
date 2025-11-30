package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para los items de una orden
 * Corresponde a la estructura del carrito del frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item de producto en la orden")
public class OrderItemDTO {

    @NotBlank(message = "El código del producto es requerido")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    @Schema(description = "Código único del producto", example = "TC001")
    private String code;

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    @Schema(description = "Nombre del producto", example = "Torta Cuadrada de Chocolate")
    private String name;

    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio unitario en CLP", example = "45000")
    private BigDecimal priceCLP;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Schema(description = "Cantidad de unidades", example = "2")
    private Integer qty;

    @Schema(description = "Opciones de personalización del producto")
    private ProductOptionsDTO opciones;
}
