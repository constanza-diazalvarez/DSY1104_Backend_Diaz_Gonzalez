package com.milsabores.ventas.controller;

import com.milsabores.ventas.dto.*;
import com.milsabores.ventas.entity.OrderStatus;
import com.milsabores.ventas.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para la gestión de órdenes
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Órdenes", description = "API para la gestión de órdenes de compra")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Crear una nueva orden", description = "Crea una nueva orden de compra a partir del carrito del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la orden inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una orden con ese número")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO request) {
        OrderResponseDTO order = orderService.createOrder(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Orden creada exitosamente", order));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Obtiene los detalles de una orden específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderById(
            @Parameter(description = "ID de la orden") @PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Orden encontrada", order));
    }

    @GetMapping("/buyorder/{buyOrder}")
    @Operation(summary = "Obtener orden por número de orden", description = "Obtiene los detalles de una orden por su número único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderByBuyOrder(
            @Parameter(description = "Número de orden", example = "ORD-1701234567890") 
            @PathVariable String buyOrder) {
        OrderResponseDTO order = orderService.getOrderByBuyOrder(buyOrder);
        return ResponseEntity.ok(ApiResponseDTO.success("Orden encontrada", order));
    }

    @GetMapping
    @Operation(summary = "Listar todas las órdenes", description = "Obtiene todas las órdenes con paginación")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida")
    public ResponseEntity<ApiResponseDTO<Page<OrderResponseDTO>>> getAllOrders(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Órdenes obtenidas", orders));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar órdenes por estado", description = "Obtiene todas las órdenes con un estado específico")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getOrdersByStatus(
            @Parameter(description = "Estado de la orden", example = "APPROVED") 
            @PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponseDTO.success("Órdenes obtenidas", orders));
    }

    @GetMapping("/customer/{email}")
    @Operation(summary = "Listar órdenes por cliente", description = "Obtiene todas las órdenes de un cliente por su email")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getOrdersByCustomerEmail(
            @Parameter(description = "Email del cliente", example = "cliente@email.com") 
            @PathVariable String email) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(ApiResponseDTO.success("Órdenes del cliente obtenidas", orders));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de orden", description = "Actualiza el estado de una orden (pago, preparación, entrega)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> updateOrderStatus(
            @Parameter(description = "ID de la orden") @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDTO request) {
        OrderResponseDTO order = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Estado de orden actualizado", order));
    }

    @PatchMapping("/buyorder/{buyOrder}/status")
    @Operation(summary = "Actualizar estado por número de orden", 
               description = "Actualiza el estado de una orden usando su número único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> updateOrderStatusByBuyOrder(
            @Parameter(description = "Número de orden") @PathVariable String buyOrder,
            @Valid @RequestBody UpdateOrderStatusDTO request) {
        OrderResponseDTO order = orderService.updateOrderStatusByBuyOrder(buyOrder, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Estado de orden actualizado", order));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar orden", description = "Cancela una orden pendiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden cancelada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "La orden no puede ser cancelada")
    })
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> cancelOrder(
            @Parameter(description = "ID de la orden") @PathVariable Long id) {
        OrderResponseDTO order = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Orden cancelada exitosamente", order));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden", description = "Elimina una orden cancelada o rechazada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Orden eliminada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "La orden no puede ser eliminada")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID de la orden") @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recent")
    @Operation(summary = "Obtener órdenes recientes", description = "Obtiene las últimas 10 órdenes creadas")
    @ApiResponse(responseCode = "200", description = "Órdenes recientes obtenidas")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getRecentOrders() {
        List<OrderResponseDTO> orders = orderService.getRecentOrders();
        return ResponseEntity.ok(ApiResponseDTO.success("Órdenes recientes obtenidas", orders));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Obtener órdenes por rango de fechas", 
               description = "Obtiene todas las órdenes creadas entre dos fechas")
    @ApiResponse(responseCode = "200", description = "Órdenes obtenidas")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getOrdersByDateRange(
            @Parameter(description = "Fecha de inicio (ISO format)", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin (ISO format)", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderResponseDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponseDTO.success("Órdenes obtenidas", orders));
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de órdenes", 
               description = "Obtiene estadísticas generales de las órdenes")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas")
    public ResponseEntity<ApiResponseDTO<OrderStatsDTO>> getOrderStats() {
        OrderStatsDTO stats = orderService.getOrderStats();
        return ResponseEntity.ok(ApiResponseDTO.success("Estadísticas obtenidas", stats));
    }
}
