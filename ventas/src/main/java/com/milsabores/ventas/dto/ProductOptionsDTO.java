package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para las opciones de personalización de un producto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Opciones de personalización del producto")
public class ProductOptionsDTO {

    @Size(max = 50, message = "El tamaño no puede exceder 50 caracteres")
    @Schema(description = "Tamaño seleccionado", example = "12 porciones")
    private String tamano;

    @Size(max = 100, message = "El mensaje no puede exceder 100 caracteres")
    @Schema(description = "Mensaje personalizado para la torta", example = "Feliz Cumpleaños María!")
    private String mensaje;
}
