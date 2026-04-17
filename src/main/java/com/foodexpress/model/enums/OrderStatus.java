package com.foodexpress.model.enums;

import java.util.Set;
import java.util.Map;

public enum OrderStatus {
    PLACED,
    ACCEPTED,
    PREPARING,
    READY,
    DELIVERING,
    DELIVERED,
    CANCELLED;

    // Définit les transitions autorisées pour chaque état
    private static final Map<OrderStatus, Set<OrderStatus>> TRANSITIONS = Map.of(
        PLACED,     Set.of(ACCEPTED, CANCELLED),
        ACCEPTED,   Set.of(PREPARING, CANCELLED),
        PREPARING,  Set.of(READY, CANCELLED),
        READY,      Set.of(DELIVERING, CANCELLED),
        DELIVERING, Set.of(DELIVERED, CANCELLED),
        DELIVERED,  Set.of(),
        CANCELLED,  Set.of()
    );

    public boolean canTransitionTo(OrderStatus next) {
        return TRANSITIONS.getOrDefault(this, Set.of()).contains(next);
    }
}