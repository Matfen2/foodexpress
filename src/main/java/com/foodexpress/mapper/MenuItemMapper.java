package com.foodexpress.mapper;

import com.foodexpress.dto.response.MenuItemResponse;
import com.foodexpress.model.entity.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItemResponse toResponse(MenuItem entity);
}