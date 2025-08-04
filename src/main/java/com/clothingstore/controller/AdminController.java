package com.clothingstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clothingstore.model.Order;
import com.clothingstore.model.Product;
import com.clothingstore.repository.OrderRepository;
import com.clothingstore.repository.ProductRepository;
import com.clothingstore.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final ProductRepository productRepository;
//    private final Product product;
    
    

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        productRepository.save(product);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) throws MessagingException {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);

        emailService.sendOrderStatusUpdate(order.getUser().getEmail(), order.getUser().getName(), order.getId(), status);
        return ResponseEntity.ok("Order status updated and email sent");
    }
}
