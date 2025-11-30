package com.milsabores.ventas.mapper;

import com.milsabores.ventas.dto.*;
import com.milsabores.ventas.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente que mapea entre entidades y DTOs
 */
@Component
public class OrderMapper {

    /**
     * Convierte un DTO de creación a entidad Order
     */
    public Order toEntity(CreateOrderRequestDTO dto) {
        Order order = Order.builder()
                .buyOrder(dto.getBuyOrder())
                .amount(dto.getAmount())
                .discountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO)
                .finalAmount(calculateFinalAmount(dto.getAmount(), dto.getDiscountAmount()))
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()))
                .customerInfo(toCustomerInfo(dto.getCustomer()))
                .build();

        // Agregar items
        if (dto.getItems() != null) {
            dto.getItems().forEach(itemDto -> {
                OrderItem item = toOrderItem(itemDto);
                order.addItem(item);
            });
        }

        return order;
    }

    /**
     * Convierte CustomerInfoDTO a CustomerInfo embebido
     */
    public CustomerInfo toCustomerInfo(CustomerInfoDTO dto) {
        if (dto == null) return null;

        return CustomerInfo.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .comuna(dto.getComuna())
                .ciudad(dto.getCiudad())
                .build();
    }

    /**
     * Convierte OrderItemDTO a OrderItem
     */
    public OrderItem toOrderItem(OrderItemDTO dto) {
        if (dto == null) return null;

        return OrderItem.builder()
                .productCode(dto.getCode())
                .productName(dto.getName())
                .unitPrice(dto.getPriceCLP())
                .quantity(dto.getQty())
                .subtotal(dto.getPriceCLP().multiply(BigDecimal.valueOf(dto.getQty())))
                .sizeOption(dto.getOpciones() != null ? dto.getOpciones().getTamano() : null)
                .customMessage(dto.getOpciones() != null ? dto.getOpciones().getMensaje() : null)
                .build();
    }

    /**
     * Convierte una entidad Order a OrderResponseDTO
     */
    public OrderResponseDTO toResponseDTO(Order order) {
        if (order == null) return null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .buyOrder(order.getBuyOrder())
                .status(order.getStatus())
                .amount(order.getAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .authorizationCode(order.getAuthorizationCode())
                .cardNumber(order.getCardNumber())
                .paymentMethod(order.getPaymentMethod())
                .errorMessage(order.getErrorMessage())
                .createdAt(order.getCreatedAt())
                .transactionDate(order.getTransactionDate())
                .customer(toCustomerInfoDTO(order.getCustomerInfo()))
                .items(toOrderItemResponseDTOList(order.getItems()))
                .build();
    }

    /**
     * Convierte CustomerInfo a CustomerInfoDTO
     */
    public CustomerInfoDTO toCustomerInfoDTO(CustomerInfo customerInfo) {
        if (customerInfo == null) return null;

        return CustomerInfoDTO.builder()
                .nombre(customerInfo.getNombre())
                .email(customerInfo.getEmail())
                .telefono(customerInfo.getTelefono())
                .direccion(customerInfo.getDireccion())
                .comuna(customerInfo.getComuna())
                .ciudad(customerInfo.getCiudad())
                .build();
    }

    /**
     * Convierte OrderItem a OrderItemResponseDTO
     */
    public OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item) {
        if (item == null) return null;

        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .productCode(item.getProductCode())
                .productName(item.getProductName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .sizeOption(item.getSizeOption())
                .customMessage(item.getCustomMessage())
                .build();
    }

    /**
     * Convierte lista de OrderItem a lista de OrderItemResponseDTO
     */
    public List<OrderItemResponseDTO> toOrderItemResponseDTOList(List<OrderItem> items) {
        if (items == null) return null;

        return items.stream()
                .map(this::toOrderItemResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte lista de Order a lista de OrderResponseDTO
     */
    public List<OrderResponseDTO> toResponseDTOList(List<Order> orders) {
        if (orders == null) return null;

        return orders.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el monto final después de descuentos
     */
    private BigDecimal calculateFinalAmount(BigDecimal amount, BigDecimal discount) {
        if (amount == null) return BigDecimal.ZERO;
        if (discount == null) return amount;
        return amount.subtract(discount);
    }
}
