//package com.clothingstore.controller;
//
//import com.clothingstore.model.CartItem;
//import com.clothingstore.model.Order;
//import com.clothingstore.model.User;
//import com.clothingstore.repository.CartRepository;
//import com.clothingstore.repository.OrderRepository;
//import com.clothingstore.service.EmailService;
//import com.clothingstore.service.InvoiceService;
//import com.clothingstore.service.OrderService;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.ByteArrayInputStream;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/orders")
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final CartRepository cartRepository;
//    private final OrderRepository orderRepository;
//    private final InvoiceService invoiceService;
//    private final EmailService emailService;
//    private final OrderService orderService;
//    
//
//    @PostMapping("/place")
//    public ResponseEntity<?> placeOrder(@AuthenticationPrincipal User user) throws Exception {
//        List<CartItem> cartItems = cartRepository.findByUser(user);
//        if (cartItems.isEmpty()) {
//            return ResponseEntity.badRequest().body("Cart is empty");
//        }
//
//        double total = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
//
//        Order order = Order.builder()
//                .user(user)
//                .status("PAID")
//                .totalPrice(total)
//                .orderDate(LocalDateTime.now())
//                .build();
//
//        orderRepository.save(order);
//        cartRepository.deleteByUser(user);
//
//        // Generate PDF Invoice
//        ByteArrayInputStream invoicePdf = invoiceService.generateInvoice(order);
//
//        // Send email with invoice
//        emailService.sendInvoiceEmail(user.getEmail(), user.getName(), order.getId(), total, invoicePdf);
//
//        return ResponseEntity.ok("Order placed successfully");
//    }
//
//    @GetMapping
//    public List<Order> getOrders(@AuthenticationPrincipal User user) {
//        return orderRepository.findByUser(user);
//    }
//}
package com.clothingstore.controller;

import com.clothingstore.model.Order;
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
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal User user) {
        Order order = orderService.placeOrder(user);
        return ResponseEntity.ok(order);
    }
}
