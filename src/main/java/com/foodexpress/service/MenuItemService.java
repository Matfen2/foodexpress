package com.foodexpress.service;

import com.foodexpress.dto.request.CreateMenuItemRequest;
import com.foodexpress.dto.response.MenuItemResponse;
import com.foodexpress.exception.ResourceNotFoundException;
import com.foodexpress.mapper.MenuItemMapper;
import com.foodexpress.model.entity.MenuItem;
import com.foodexpress.model.entity.Restaurant;
import com.foodexpress.repository.MenuItemRepository;
import com.foodexpress.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    @Transactional
    public MenuItemResponse create(CreateMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant #" + request.getRestaurantId() + " introuvable"));

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .restaurant(restaurant)
                .build();

        return menuItemMapper.toResponse(menuItemRepository.save(item));
    }

    public List<MenuItemResponse> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId)
                .stream().map(menuItemMapper::toResponse).toList();
    }
}