package com.foodexpress.mapper;

import com.foodexpress.dto.request.CreateRestaurantRequest;
import com.foodexpress.dto.response.RestaurantResponse;
import com.foodexpress.model.entity.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toResponse(Restaurant entity);

    Restaurant toEntity(CreateRestaurantRequest request);
}