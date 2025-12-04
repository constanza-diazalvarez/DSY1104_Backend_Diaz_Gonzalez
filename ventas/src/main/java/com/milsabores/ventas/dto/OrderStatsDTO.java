package com.milsabores.ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para estadísticas de órdenes
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Estadísticas de órdenes")
public class OrderStatsDTO {

    @Schema(description = "Total de órdenes", example = "150")
    private Long totalOrders;

    @Schema(description = "Órdenes pendientes", example = "10")
    private Long pendingOrders;

    @Schema(description = "Órdenes aprobadas", example = "120")
    private Long approvedOrders;

    @Schema(description = "Órdenes rechazadas", example = "15")
    private Long rejectedOrders;

    @Schema(description = "Órdenes canceladas", example = "5")
    private Long cancelledOrders;

    @Schema(description = "Monto total vendido", example = "15000000")
    private BigDecimal totalRevenue;

    @Schema(description = "Promedio por orden", example = "100000")
    private BigDecimal averageOrderAmount;
}
