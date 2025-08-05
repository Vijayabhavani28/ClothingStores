package com.clothingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice;
}
