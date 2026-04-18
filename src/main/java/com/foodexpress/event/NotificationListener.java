package com.foodexpress.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    @Async
    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("[NOTIFICATION] Commande #{} reçue — notification envoyée au restaurant #{}",
                event.orderId(), event.restaurantId());
    }

    @Async
    @EventListener
    public void onStatusChanged(OrderStatusChangedEvent event) {
        log.info("[NOTIFICATION] Commande #{} : {} → {} — client notifié",
                event.orderId(), event.oldStatus(), event.newStatus());
    }
}