package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para crear una nueva orden
 * Recibe los datos del checkout del frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos requeridos para crear una nueva orden")
public class CreateOrderRequestDTO {

    @NotBlank(message = "El número de orden es requerido")
    @Size(max = 50, message = "El número de orden no puede exceder 50 caracteres")
    @Schema(description = "Número único de orden", example = "ORD-1701234567890")
    private String buyOrder;

    @NotNull(message = "El monto total es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto total de la orden en CLP", example = "45000")
    private BigDecimal amount;

    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @Schema(description = "Monto de descuento aplicado", example = "5000")
    private BigDecimal discountAmount;

    @NotBlank(message = "El método de pago es requerido")
    @Pattern(regexp = "WEBPAY|TRANSFERENCIA|EFECTIVO", message = "Método de pago inválido")
    @Schema(description = "Método de pago seleccionado", example = "WEBPAY")
    private String paymentMethod;

    @NotNull(message = "La información del cliente es requerida")
    @Valid
    @Schema(description = "Datos del cliente")
    private CustomerInfoDTO customer;

    @NotEmpty(message = "La orden debe tener al menos un producto")
    @Valid
    @Schema(description = "Lista de productos en la orden")
    private List<OrderItemDTO> items;
}
