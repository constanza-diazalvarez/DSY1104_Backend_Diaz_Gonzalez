package com.milsabores.ventas.dto.transbank;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO para iniciar un pago desde el frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request para iniciar un pago con WebPay")
public class InitPaymentRequestDTO {

    @NotBlank(message = "El número de orden es requerido")
    @Schema(description = "Número único de la orden", example = "ORD-1701234567890")
    private String buyOrder;

    @NotNull(message = "El monto es requerido")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto total a pagar en CLP", example = "50000")
    private Integer amount;

    @Schema(description = "ID de sesión opcional del usuario", example = "user-session-abc123")
    private String sessionId;
}
