package com.clothingstore.controller;

import com.clothingstore.dto.OrderResponseDTO;
import com.clothingstore.model.User;
import com.clothingstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.placeOrder(user));
    }
}
