package com.clothingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<CartItemDTO> items;
    private double grandTotal;
}
