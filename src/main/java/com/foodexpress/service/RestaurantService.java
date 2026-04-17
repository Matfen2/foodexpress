package com.foodexpress.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RestaurantService {

    public void creerRestaurant(String nom) {
        log.info("Création du restaurant : {}", nom);
        // ... logique métier ...
        log.debug("Restaurant créé avec succès : {}", nom);
    }
}