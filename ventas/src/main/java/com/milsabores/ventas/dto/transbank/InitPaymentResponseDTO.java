package com.milsabores.ventas.dto.transbank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para la respuesta de inicio de pago
 * El frontend debe redirigir al usuario a url + "?token_ws=" + token
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos para redirigir a WebPay")
public class InitPaymentResponseDTO {

    @Schema(description = "Token de la transacción", example = "01ab1cc456def...")
    private String token;

    @Schema(description = "URL base del formulario de pago", example = "https://webpay3gint.transbank.cl/webpayserver/initTransaction")
    private String url;

    @Schema(description = "URL completa para redirigir (url + token)", example = "https://webpay3gint.transbank.cl/webpayserver/initTransaction?token_ws=01ab...")
    private String redirectUrl;

    @Schema(description = "Número de orden asociado", example = "ORD-1701234567890")
    private String buyOrder;
}
