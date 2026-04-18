package com.foodexpress.event;

import com.foodexpress.model.enums.OrderStatus;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus oldStatus,
    OrderStatus newStatus
) {}