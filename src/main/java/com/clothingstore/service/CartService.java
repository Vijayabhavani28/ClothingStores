package com.clothingstore.service;

import com.clothingstore.model.*;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // Get user's cart
    public Cart getCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    // Add product to cart
    public Cart addToCart(User user, Long productId, int quantity) {
        Cart cart = getCart(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity if item already exists
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    // Remove product from cart
    public Cart removeFromCart(User user, Long productId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    // Clear cart
    public void clearCart(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
