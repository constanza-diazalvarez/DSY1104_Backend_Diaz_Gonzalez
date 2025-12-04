package com.milsabores.ventas.dto.transbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO para la respuesta de confirmación de transacción en WebPay
 * Contiene todos los datos del resultado de la transacción
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de confirmación de transacción WebPay")
public class WebPayCommitResponse {

    @Schema(description = "Resultado de la autorización", example = "0")
    @JsonProperty("vci")
    private String vci;

    @Schema(description = "Monto de la transacción", example = "50000")
    private int amount;

    @Schema(description = "Código de respuesta de la autorización", example = "0")
    @JsonProperty("status")
    private String status;

    @Schema(description = "Orden de compra del comercio", example = "ORD-1701234567890")
    @JsonProperty("buy_order")
    private String buyOrder;

    @Schema(description = "Identificador de sesión", example = "session-123456")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(description = "Número de tarjeta (últimos 4 dígitos)", example = "6623")
    @JsonProperty("card_detail")
    private CardDetail cardDetail;

    @Schema(description = "Fecha contable de la transacción", example = "1130")
    @JsonProperty("accounting_date")
    private String accountingDate;

    @Schema(description = "Fecha y hora de la transacción", example = "2024-11-30T15:30:00.000Z")
    @JsonProperty("transaction_date")
    private String transactionDate;

    @Schema(description = "Código de autorización", example = "1213")
    @JsonProperty("authorization_code")
    private String authorizationCode;

    @Schema(description = "Tipo de pago", example = "VN")
    @JsonProperty("payment_type_code")
    private String paymentTypeCode;

    @Schema(description = "Código de respuesta", example = "0")
    @JsonProperty("response_code")
    private int responseCode;

    @Schema(description = "Cantidad de cuotas", example = "0")
    @JsonProperty("installments_number")
    private int installmentsNumber;

    @Schema(description = "Monto de cada cuota", example = "0")
    @JsonProperty("installments_amount")
    private Integer installmentsAmount;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardDetail {
        @JsonProperty("card_number")
        private String cardNumber;
    }

    /**
     * Verifica si la transacción fue aprobada
     */
    public boolean isApproved() {
        return responseCode == 0 && "AUTHORIZED".equals(status);
    }
}
