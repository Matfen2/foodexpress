package com.foodexpress.config;

import com.foodexpress.model.entity.*;
import com.foodexpress.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Slf4j
@Component
@Profile("dev")  // s'exécute UNIQUEMENT en mode développement
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        // Restaurant 1
        Restaurant pizza = restaurantRepository.save(
                Restaurant.builder().name("Pizza Palace").address("10 Rue de Rome").phone("0611111111").build());

        menuItemRepository.save(MenuItem.builder().name("Margherita").price(new BigDecimal("9.50")).restaurant(pizza).build());
        menuItemRepository.save(MenuItem.builder().name("Pepperoni").price(new BigDecimal("12.00")).restaurant(pizza).build());
        menuItemRepository.save(MenuItem.builder().name("4 Fromages").price(new BigDecimal("13.50")).restaurant(pizza).build());

        // Restaurant 2
        Restaurant sushi = restaurantRepository.save(
                Restaurant.builder().name("Sushi Master").address("25 Av. du Japon").phone("0622222222").build());

        menuItemRepository.save(MenuItem.builder().name("Saumon Roll").price(new BigDecimal("8.00")).restaurant(sushi).build());
        menuItemRepository.save(MenuItem.builder().name("Tempura").price(new BigDecimal("11.00")).restaurant(sushi).build());

        // Restaurant 3
        Restaurant burger = restaurantRepository.save(
                Restaurant.builder().name("Burger Factory").address("5 Bd des Grillades").phone("0633333333").build());

        menuItemRepository.save(MenuItem.builder().name("Classic Burger").price(new BigDecimal("10.00")).restaurant(burger).build());
        menuItemRepository.save(MenuItem.builder().name("Cheese Burger").price(new BigDecimal("11.50")).restaurant(burger).build());

        // Clients
        customerRepository.save(Customer.builder().firstName("Sara").lastName("Benali").email("sara@mail.com").address("Casablanca").build());
        customerRepository.save(Customer.builder().firstName("Youssef").lastName("Amrani").email("youssef@mail.com").address("Rabat").build());

        log.info("✅ Données de test insérées : 3 restaurants, 7 items, 2 clients");
    }
}