package com.foodexpress.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "L'ID du menu item est obligatoire")
    private Long menuItemId;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    private int quantity;
}