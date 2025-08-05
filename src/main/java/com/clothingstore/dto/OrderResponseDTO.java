//package com.clothingstore.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@AllArgsConstructor
////@NoArgsConstructor
//public class OrderResponseDTO {
//    private Long orderId;
//    private List<ProductInfo> products;
//    private double totalPrice;
//    private String status;
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class ProductInfo {
//        private String name;
//        private double price;
//        private int quantity;
//    }
//}


package com.clothingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private List<OrderItemDTO> items;
    private double totalPrice;
    private String status;
}
