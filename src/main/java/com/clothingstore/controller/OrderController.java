package com.clothingstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clothingstore.dto.OrderResponseDTO;
//import com.clothingstore.model.Order;
import com.clothingstore.model.User;
import com.clothingstore.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.placeOrder(user));
        
    }
    @GetMapping
    public List<OrderResponseDTO> getUserOrders(@AuthenticationPrincipal User user) {
        return orderService.getUserOrders(user);
    }
    
}
