package com.foodexpress.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
}