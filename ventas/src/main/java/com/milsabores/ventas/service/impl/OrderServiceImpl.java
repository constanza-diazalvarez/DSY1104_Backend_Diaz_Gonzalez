package com.milsabores.ventas.service.impl;

import com.milsabores.ventas.dto.*;
import com.milsabores.ventas.entity.Order;
import com.milsabores.ventas.entity.OrderStatus;
import com.milsabores.ventas.exception.OrderNotFoundException;
import com.milsabores.ventas.exception.DuplicateOrderException;
import com.milsabores.ventas.exception.InvalidOrderOperationException;
import com.milsabores.ventas.mapper.OrderMapper;
import com.milsabores.ventas.repository.OrderRepository;
import com.milsabores.ventas.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de órdenes
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request) {
        log.info("Creando nueva orden: {}", request.getBuyOrder());

        // Verificar que no exista una orden con el mismo número
        if (orderRepository.existsByBuyOrder(request.getBuyOrder())) {
            throw new DuplicateOrderException("Ya existe una orden con el número: " + request.getBuyOrder());
        }

        // Convertir DTO a entidad
        Order order = orderMapper.toEntity(request);

        // Guardar orden
        Order savedOrder = orderRepository.save(order);
        log.info("Orden creada exitosamente con ID: {}", savedOrder.getId());

        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        log.debug("Buscando orden por ID: {}", id);
        Order order = findOrderByIdOrThrow(id);
        return orderMapper.toResponseDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderByBuyOrder(String buyOrder) {
        log.debug("Buscando orden por número: {}", buyOrder);
        Order order = orderRepository.findByBuyOrder(buyOrder)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró la orden: " + buyOrder));
        return orderMapper.toResponseDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        log.debug("Obteniendo todas las órdenes con paginación");
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        log.debug("Buscando órdenes por estado: {}", status);
        List<Order> orders = orderRepository.findByStatus(status);
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        log.debug("Buscando órdenes por estado con paginación: {}", status);
        return orderRepository.findByStatus(status, pageable)
                .map(orderMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerEmail(String email) {
        log.debug("Buscando órdenes por email: {}", email);
        List<Order> orders = orderRepository.findByCustomerInfoEmail(email);
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, UpdateOrderStatusDTO request) {
        log.info("Actualizando estado de orden ID: {} a {}", id, request.getStatus());
        
        Order order = findOrderByIdOrThrow(id);
        updateOrderStatusFields(order, request);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Estado de orden actualizado exitosamente");
        
        return orderMapper.toResponseDTO(updatedOrder);
    }

    @Override
    public OrderResponseDTO updateOrderStatusByBuyOrder(String buyOrder, UpdateOrderStatusDTO request) {
        log.info("Actualizando estado de orden {} a {}", buyOrder, request.getStatus());
        
        Order order = orderRepository.findByBuyOrder(buyOrder)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró la orden: " + buyOrder));
        
        updateOrderStatusFields(order, request);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Estado de orden actualizado exitosamente");
        
        return orderMapper.toResponseDTO(updatedOrder);
    }

    @Override
    public OrderResponseDTO cancelOrder(Long id) {
        log.info("Cancelando orden ID: {}", id);
        
        Order order = findOrderByIdOrThrow(id);
        
        // Solo se pueden cancelar órdenes pendientes
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderOperationException(
                "Solo se pueden cancelar órdenes pendientes. Estado actual: " + order.getStatus());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);
        
        log.info("Orden cancelada exitosamente");
        return orderMapper.toResponseDTO(cancelledOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Eliminando orden ID: {}", id);
        
        Order order = findOrderByIdOrThrow(id);
        
        // Solo se pueden eliminar órdenes canceladas o rechazadas
        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.REJECTED) {
            throw new InvalidOrderOperationException(
                "Solo se pueden eliminar órdenes canceladas o rechazadas. Estado actual: " + order.getStatus());
        }
        
        orderRepository.delete(order);
        log.info("Orden eliminada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getRecentOrders() {
        log.debug("Obteniendo últimas órdenes");
        List<Order> orders = orderRepository.findTop10ByOrderByCreatedAtDesc();
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Buscando órdenes entre {} y {}", startDate, endDate);
        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
        return orderMapper.toResponseDTOList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatsDTO getOrderStats() {
        log.debug("Calculando estadísticas de órdenes");
        
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long approvedOrders = orderRepository.countByStatus(OrderStatus.APPROVED);
        long rejectedOrders = orderRepository.countByStatus(OrderStatus.REJECTED);
        long cancelledOrders = orderRepository.countByStatus(OrderStatus.CANCELLED);
        
        // Calcular ingresos totales de órdenes aprobadas
        List<Order> approved = orderRepository.findByStatus(OrderStatus.APPROVED);
        BigDecimal totalRevenue = approved.stream()
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageOrderAmount = totalOrders > 0 
                ? totalRevenue.divide(BigDecimal.valueOf(approvedOrders > 0 ? approvedOrders : 1), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        
        return OrderStatsDTO.builder()
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .approvedOrders(approvedOrders)
                .rejectedOrders(rejectedOrders)
                .cancelledOrders(cancelledOrders)
                .totalRevenue(totalRevenue)
                .averageOrderAmount(averageOrderAmount)
                .build();
    }

    /**
     * Busca una orden por ID o lanza excepción
     */
    private Order findOrderByIdOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró la orden con ID: " + id));
    }

    /**
     * Actualiza los campos de estado de una orden
     */
    private void updateOrderStatusFields(Order order, UpdateOrderStatusDTO request) {
        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());
        order.setStatus(newStatus);
        
        if (newStatus == OrderStatus.APPROVED) {
            order.setAuthorizationCode(request.getAuthorizationCode());
            order.setCardNumber(request.getCardNumber());
            order.setTransactionDate(LocalDateTime.now());
        } else if (newStatus == OrderStatus.REJECTED) {
            order.setErrorMessage(request.getErrorMessage());
            order.setTransactionDate(LocalDateTime.now());
        }
    }
}
