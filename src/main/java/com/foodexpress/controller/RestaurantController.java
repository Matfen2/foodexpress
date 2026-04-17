package com.foodexpress.controller;

import com.foodexpress.dto.request.CreateRestaurantRequest;
import com.foodexpress.dto.response.RestaurantResponse;
import com.foodexpress.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@Valid @RequestBody CreateRestaurantRequest request) {
        return restaurantService.create(request);
    }

    @GetMapping
    public List<RestaurantResponse> getAll(@RequestParam(required = false) String name) {
        return restaurantService.getAll(name);
    }

    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        return restaurantService.getById(id);
    }

    @PatchMapping("/{id}/deactivate")
    public RestaurantResponse deactivate(@PathVariable Long id) {
        return restaurantService.deactivate(id);
    }
}