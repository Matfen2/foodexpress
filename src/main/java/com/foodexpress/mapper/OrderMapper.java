package com.foodexpress.mapper;

import com.foodexpress.dto.response.OrderItemResponse;
import com.foodexpress.dto.response.OrderResponse;
import com.foodexpress.model.entity.Order;
import com.foodexpress.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerName",
             expression = "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    OrderResponse toResponse(Order order);

    @Mapping(target = "menuItemName", source = "menuItem.name")
    OrderItemResponse toItemResponse(OrderItem item);
}