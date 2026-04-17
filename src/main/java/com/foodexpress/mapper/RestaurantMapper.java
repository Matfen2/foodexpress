package com.foodexpress.mapper;

import com.foodexpress.dto.request.CreateRestaurantRequest;
import com.foodexpress.dto.response.RestaurantResponse;
import com.foodexpress.model.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toResponse(Restaurant entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toEntity(CreateRestaurantRequest request);
}