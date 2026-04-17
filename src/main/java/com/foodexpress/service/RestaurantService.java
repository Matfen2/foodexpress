package com.foodexpress.service;

import com.foodexpress.dto.request.CreateRestaurantRequest;
import com.foodexpress.dto.response.RestaurantResponse;
import com.foodexpress.exception.ResourceNotFoundException;
import com.foodexpress.mapper.RestaurantMapper;
import com.foodexpress.model.entity.Restaurant;
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
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponse create(CreateRestaurantRequest request) {
        log.info("Création du restaurant : {}", request.getName());
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(saved);
    }

    public RestaurantResponse getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant #" + id + " introuvable"));
        return restaurantMapper.toResponse(restaurant);
    }

    public List<RestaurantResponse> getAll(String name) {
        List<Restaurant> restaurants;
        if (name != null && !name.isBlank()) {
            restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
        } else {
            restaurants = restaurantRepository.findByActiveTrue();
        }
        return restaurants.stream().map(restaurantMapper::toResponse).toList();
    }

    @Transactional
    public RestaurantResponse deactivate(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant #" + id + " introuvable"));
        restaurant.setActive(false);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }
}