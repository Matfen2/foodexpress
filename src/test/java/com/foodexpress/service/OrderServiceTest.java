package com.foodexpress.service;

import com.foodexpress.dto.request.*;
import com.foodexpress.exception.*;
import com.foodexpress.mapper.OrderMapper;
import com.foodexpress.model.entity.*;
import com.foodexpress.model.enums.OrderStatus;
import com.foodexpress.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock CustomerRepository customerRepository;
    @Mock RestaurantRepository restaurantRepository;
    @Mock MenuItemRepository menuItemRepository;
    @Mock OrderMapper orderMapper;
    @Mock org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks OrderService orderService;

    @Test
    void placeOrder_restaurantInactive_shouldThrow() {
        // Given
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test")
                .active(false)
                .build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setItems(List.of());

        // When / Then
        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("fermé");
    }

    @Test
    void updateStatus_invalidTransition_shouldThrow() {
        // Given
        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.PLACED)
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setNewStatus(OrderStatus.DELIVERED);

        // When / Then
        assertThatThrownBy(() -> orderService.updateStatus(1L, request))
                .isInstanceOf(InvalidOrderStateException.class)
                .hasMessageContaining("PLACED")
                .hasMessageContaining("DELIVERED");
    }
}