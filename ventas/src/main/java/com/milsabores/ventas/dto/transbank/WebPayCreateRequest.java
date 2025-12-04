package com.milsabores.ventas.dto.transbank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para iniciar una transacción en WebPay
 * Corresponde al request de /rswebpaytransaction/api/webpay/v1.2/transactions
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request para crear una transacción en WebPay")
public class WebPayCreateRequest {

    @Schema(description = "Orden de compra del comercio", example = "ORD-1701234567890")
    private String buy_order;

    @Schema(description = "Identificador de sesión del comercio", example = "session-123456")
    private String session_id;

    @Schema(description = "Monto de la transacción en pesos chilenos", example = "50000")
    private int amount;

    @Schema(description = "URL de retorno del comercio", example = "http://localhost:5173/pago-resultado")
    private String return_url;
}
