package com.milsabores.ventas.dto.transbank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta del resultado del pago
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resultado del pago procesado")
public class PaymentResultDTO {

    @Schema(description = "Indica si el pago fue exitoso", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Pago aprobado exitosamente")
    private String message;

    @Schema(description = "Número de orden", example = "ORD-1701234567890")
    private String buyOrder;

    @Schema(description = "Monto pagado", example = "50000")
    private Integer amount;

    @Schema(description = "Código de autorización", example = "1213")
    private String authorizationCode;

    @Schema(description = "Últimos 4 dígitos de la tarjeta", example = "6623")
    private String cardNumber;

    @Schema(description = "Tipo de pago (VN=Venta Normal, VC=Venta en Cuotas)", example = "VN")
    private String paymentType;

    @Schema(description = "Número de cuotas", example = "0")
    private Integer installments;

    @Schema(description = "Fecha de la transacción")
    private LocalDateTime transactionDate;

    @Schema(description = "Código de respuesta (0=Aprobado)", example = "0")
    private Integer responseCode;

    @Schema(description = "Estado de la orden", example = "APPROVED")
    private String orderStatus;
}
