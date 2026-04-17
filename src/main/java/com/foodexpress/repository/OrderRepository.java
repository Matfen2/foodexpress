package com.foodexpress.repository;

import com.foodexpress.model.entity.Order;
import com.foodexpress.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    // Requête JPQL personnalisée : commandes passées dans les N dernières heures
    @Query("SELECT o FROM Order o WHERE o.placedAt >= :since")
    List<Order> findRecentOrders(@Param("since") LocalDateTime since);
}