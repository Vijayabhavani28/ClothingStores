package com.clothingstore.service;

import com.clothingstore.dto.OrderResponseDTO;
import com.clothingstore.model.*;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService; // Added email service

    public OrderResponseDTO placeOrder(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setItems(new ArrayList<>());

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getItems().add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // Send email notification
        emailService.sendOrderStatusUpdate(order);

        // Build response
        List<OrderResponseDTO.ProductInfo> productInfoList = order.getItems().stream()
                .map(item -> new OrderResponseDTO.ProductInfo(
                        item.getProduct().getName(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(order.getId(), productInfoList, totalPrice, order.getStatus());
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);

        // Send email notification on status update
        emailService.sendOrderStatusUpdate(order);

        return order;
    }
}
