package com.foodexpress.repository;

import java.util.List;
import com.foodexpress.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByActiveTrue();

    List<Restaurant> findByNameContainingIgnoreCase(String name);
}