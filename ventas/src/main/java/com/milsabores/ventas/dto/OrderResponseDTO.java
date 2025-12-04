package com.milsabores.ventas.dto;

import com.milsabores.ventas.entity.OrderStatus;
import com.milsabores.ventas.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de una orden
 * Contiene todos los datos de la orden para visualización
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con los datos completos de una orden")
public class OrderResponseDTO {

    @Schema(description = "ID interno de la orden", example = "1")
    private Long id;

    @Schema(description = "Número único de orden", example = "ORD-1701234567890")
    private String buyOrder;

    @Schema(description = "Estado de la orden", example = "APPROVED")
    private OrderStatus status;

    @Schema(description = "Monto subtotal en CLP", example = "90000")
    private BigDecimal amount;

    @Schema(description = "Monto de descuento aplicado", example = "5000")
    private BigDecimal discountAmount;

    @Schema(description = "Monto final después de descuentos", example = "85000")
    private BigDecimal finalAmount;

    @Schema(description = "Código de autorización del pago", example = "123456")
    private String authorizationCode;

    @Schema(description = "Últimos 4 dígitos de la tarjeta", example = "6623")
    private String cardNumber;

    @Schema(description = "Método de pago utilizado", example = "WEBPAY")
    private PaymentMethod paymentMethod;

    @Schema(description = "Mensaje de error si el pago falló")
    private String errorMessage;

    @Schema(description = "Fecha de creación de la orden")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de la transacción de pago")
    private LocalDateTime transactionDate;

    @Schema(description = "Información del cliente")
    private CustomerInfoDTO customer;

    @Schema(description = "Lista de productos en la orden")
    private List<OrderItemResponseDTO> items;
}
