package com.milsabores.ventas.dto.transbank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para la respuesta de creación de transacción en WebPay
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de creación de transacción WebPay")
public class WebPayCreateResponse {

    @Schema(description = "Token único de la transacción", example = "01ab1cc456def...")
    private String token;

    @Schema(description = "URL del formulario de pago de WebPay", example = "https://webpay3gint.transbank.cl/webpayserver/initTransaction")
    private String url;
}
