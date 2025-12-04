package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * DTO para actualizar el estado de una orden
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para actualizar el estado de una orden")
public class UpdateOrderStatusDTO {

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "PENDING|APPROVED|REJECTED|CANCELLED|PROCESSING|READY|DELIVERED", 
             message = "Estado inválido")
    @Schema(description = "Nuevo estado de la orden", example = "PROCESSING")
    private String status;

    @Schema(description = "Código de autorización (para pagos aprobados)", example = "123456")
    private String authorizationCode;

    @Schema(description = "Últimos 4 dígitos de la tarjeta", example = "6623")
    private String cardNumber;

    @Schema(description = "Mensaje de error (para pagos rechazados)", example = "Fondos insuficientes")
    private String errorMessage;
}
