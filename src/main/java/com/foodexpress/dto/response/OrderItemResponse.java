package com.foodexpress.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String menuItemName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}