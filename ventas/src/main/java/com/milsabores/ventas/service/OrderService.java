package com.milsabores.ventas.service;

import com.milsabores.ventas.dto.*;
import com.milsabores.ventas.entity.Order;
import com.milsabores.ventas.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz del servicio de órdenes
 */
public interface OrderService {

    /**
     * Crea una nueva orden
     */
    OrderResponseDTO createOrder(CreateOrderRequestDTO request);

    /**
     * Obtiene una orden por ID
     */
    OrderResponseDTO getOrderById(Long id);

    /**
     * Obtiene una orden por número de orden
     */
    OrderResponseDTO getOrderByBuyOrder(String buyOrder);

    /**
     * Obtiene todas las órdenes con paginación
     */
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    /**
     * Obtiene órdenes por estado
     */
    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

    /**
     * Obtiene órdenes por estado con paginación
     */
    Page<OrderResponseDTO> getOrdersByStatus(OrderStatus status, Pageable pageable);

    /**
     * Obtiene órdenes por email del cliente
     */
    List<OrderResponseDTO> getOrdersByCustomerEmail(String email);

    /**
     * Actualiza el estado de una orden
     */
    OrderResponseDTO updateOrderStatus(Long id, UpdateOrderStatusDTO request);

    /**
     * Actualiza el estado de una orden por número de orden
     */
    OrderResponseDTO updateOrderStatusByBuyOrder(String buyOrder, UpdateOrderStatusDTO request);

    /**
     * Cancela una orden
     */
    OrderResponseDTO cancelOrder(Long id);

    /**
     * Elimina una orden (solo si está cancelada o rechazada)
     */
    void deleteOrder(Long id);

    /**
     * Obtiene las últimas órdenes
     */
    List<OrderResponseDTO> getRecentOrders();

    /**
     * Obtiene órdenes por rango de fechas
     */
    List<OrderResponseDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene estadísticas de órdenes
     */
    OrderStatsDTO getOrderStats();
}
