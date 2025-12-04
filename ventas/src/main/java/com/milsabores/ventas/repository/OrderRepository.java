package com.milsabores.ventas.repository;

import com.milsabores.ventas.entity.Order;
import com.milsabores.ventas.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Order
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca una orden por su número de orden único
     */
    Optional<Order> findByBuyOrder(String buyOrder);

    /**
     * Verifica si existe una orden con el número dado
     */
    boolean existsByBuyOrder(String buyOrder);

    /**
     * Busca órdenes por estado
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Busca órdenes por estado con paginación
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Busca órdenes por email del cliente
     */
    List<Order> findByCustomerInfoEmail(String email);

    /**
     * Busca órdenes por email del cliente con paginación
     */
    Page<Order> findByCustomerInfoEmail(String email, Pageable pageable);

    /**
     * Busca órdenes creadas entre dos fechas
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca órdenes por estado y rango de fechas
     */
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByStatusAndDateRange(
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Obtiene el conteo de órdenes por estado
     */
    long countByStatus(OrderStatus status);

    /**
     * Busca órdenes que contienen un producto específico
     */
    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.productCode = :productCode")
    List<Order> findByProductCode(@Param("productCode") String productCode);

    /**
     * Obtiene las últimas N órdenes
     */
    List<Order> findTop10ByOrderByCreatedAtDesc();

    /**
     * Busca órdenes por teléfono del cliente
     */
    List<Order> findByCustomerInfoTelefono(String telefono);
}
