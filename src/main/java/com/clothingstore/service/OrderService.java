package com.clothingstore.service;

import com.clothingstore.model.Order;
import com.clothingstore.model.User;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Order placeOrder(User user) {
        // First, clear user's cart before placing order (if needed)
        cartRepository.deleteByUser(user);  // ✅ this requires a transaction

        // Create a new order and save it
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");

        return orderRepository.save(order);
    }
}
