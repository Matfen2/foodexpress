package com.foodexpress.dto.request;

import com.foodexpress.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Le nouveau statut est obligatoire")
    private OrderStatus newStatus;
}