package com.milsabores.ventas.repository;

import com.milsabores.ventas.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad OrderItem
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Busca items por código de producto
     */
    List<OrderItem> findByProductCode(String productCode);

    /**
     * Busca items de una orden específica
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Obtiene los productos más vendidos (top N)
     */
    @Query("SELECT i.productCode, i.productName, SUM(i.quantity) as totalQty " +
           "FROM OrderItem i " +
           "GROUP BY i.productCode, i.productName " +
           "ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts();

    /**
     * Cuenta la cantidad total vendida de un producto
     */
    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM OrderItem i WHERE i.productCode = :productCode")
    Long countTotalQuantityByProductCode(@Param("productCode") String productCode);
}
