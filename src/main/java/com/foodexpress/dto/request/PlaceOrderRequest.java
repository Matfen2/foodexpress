package com.foodexpress.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PlaceOrderRequest {

    @NotNull(message = "L'ID du client est obligatoire")
    private Long customerId;

    @NotNull(message = "L'ID du restaurant est obligatoire")
    private Long restaurantId;

    @NotEmpty(message = "La commande doit contenir au moins un item")
    @Valid  // valide aussi chaque OrderItemRequest
    private List<OrderItemRequest> items;
}