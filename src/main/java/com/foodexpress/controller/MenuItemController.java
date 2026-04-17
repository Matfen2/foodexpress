package com.foodexpress.controller;

import com.foodexpress.dto.request.CreateMenuItemRequest;
import com.foodexpress.dto.response.MenuItemResponse;
import com.foodexpress.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@Valid @RequestBody CreateMenuItemRequest request) {
        return menuItemService.create(request);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long restaurantId) {
        return menuItemService.getByRestaurant(restaurantId);
    }
}