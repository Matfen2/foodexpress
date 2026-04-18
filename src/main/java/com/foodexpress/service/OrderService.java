package com.foodexpress.service;

import com.foodexpress.dto.request.PlaceOrderRequest;
import com.foodexpress.dto.request.UpdateOrderStatusRequest;
import com.foodexpress.dto.response.OrderResponse;
import com.foodexpress.event.OrderPlacedEvent;
import com.foodexpress.event.OrderStatusChangedEvent;
import com.foodexpress.exception.*;
import com.foodexpress.mapper.OrderMapper;
import com.foodexpress.model.entity.*;
import com.foodexpress.model.enums.OrderStatus;
import com.foodexpress.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // 1. Vérifier que le client existe
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client #" + request.getCustomerId() + " introuvable"));

        // 2. Vérifier que le restaurant existe et est actif
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant #" + request.getRestaurantId() + " introuvable"));
        if (!restaurant.isActive()) {
            throw new BusinessRuleException("Le restaurant " + restaurant.getName() + " est fermé");
        }

        // 3. Créer la commande
        Order order = Order.builder()
                .customer(customer)
                .restaurant(restaurant)
                .status(OrderStatus.PLACED)
                .totalAmount(BigDecimal.ZERO)
                .build();

        // 4. Ajouter les items et calculer le total
        BigDecimal total = BigDecimal.ZERO;

        for (var itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Menu item #" + itemRequest.getMenuItemId() + " introuvable"));

            if (!menuItem.isAvailable()) {
                throw new BusinessRuleException(menuItem.getName() + " n'est pas disponible");
            }

            // Vérifier que l'item appartient au bon restaurant
            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BusinessRuleException(
                        menuItem.getName() + " n'appartient pas au restaurant " + restaurant.getName());
            }

            BigDecimal subtotal = menuItem.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .menuItem(menuItem)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(menuItem.getPrice())
                    .subtotal(subtotal)
                    .build();

            order.addItem(orderItem);
            total = total.add(subtotal);
        }

        order.setTotalAmount(total);

        // 5. Sauvegarder (cascade sauvegarde aussi les items)
        Order saved = orderRepository.save(order);
        log.info("Commande #{} créée — {} — total: {}€", saved.getId(), restaurant.getName(), total);

        // 6. Publier l'event (avant le return !)
        eventPublisher.publishEvent(new OrderPlacedEvent(
                saved.getId(), customer.getId(), restaurant.getId(), total));

        return orderMapper.toResponse(saved);
    }

    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande #" + id + " introuvable"));
        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getByCustomer(Long customerId, int page, int size) {
        return orderRepository.findByCustomerId(customerId)
                .stream().map(orderMapper::toResponse).toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande #" + id + " introuvable"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getNewStatus();

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidOrderStateException(
                    "Impossible de passer de " + currentStatus + " à " + newStatus);
        }

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        Order saved = orderRepository.save(order);
        log.info("Commande #{} : {} → {}", id, currentStatus, newStatus);

        // Publier l'event (avant le return !)
        eventPublisher.publishEvent(new OrderStatusChangedEvent(
                id, currentStatus, newStatus));

        return orderMapper.toResponse(saved);
    }
}