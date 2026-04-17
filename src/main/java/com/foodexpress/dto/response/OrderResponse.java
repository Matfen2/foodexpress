package com.foodexpress.dto.response;

import com.foodexpress.model.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private String customerName;
    private String restaurantName;
    private BigDecimal totalAmount;
    private LocalDateTime placedAt;
    private LocalDateTime deliveredAt;
    private List<OrderItemResponse> items;
}