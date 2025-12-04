package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para la respuesta de un item de orden
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalle de un item en la orden")
public class OrderItemResponseDTO {

    @Schema(description = "ID del item", example = "1")
    private Long id;

    @Schema(description = "Código del producto", example = "TC001")
    private String productCode;

    @Schema(description = "Nombre del producto", example = "Torta Cuadrada de Chocolate")
    private String productName;

    @Schema(description = "Precio unitario en CLP", example = "45000")
    private BigDecimal unitPrice;

    @Schema(description = "Cantidad de unidades", example = "2")
    private Integer quantity;

    @Schema(description = "Subtotal del item", example = "90000")
    private BigDecimal subtotal;

    @Schema(description = "Tamaño seleccionado", example = "12 porciones")
    private String sizeOption;

    @Schema(description = "Mensaje personalizado", example = "Feliz Cumpleaños María!")
    private String customMessage;
}
