package com.clothingstore.service;

import com.clothingstore.dto.CartItemDTO;
import com.clothingstore.dto.CartResponseDTO;
import com.clothingstore.model.Cart;
import com.clothingstore.model.CartItem;
import com.clothingstore.model.Product;
import com.clothingstore.model.User;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartResponseDTO addItemToCart(User user, Long productId, int quantity) {
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return buildCartResponse(cart);
    }

    public CartResponseDTO getCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return buildCartResponse(cart);
    }

    private CartResponseDTO buildCartResponse(Cart cart) {
        List<CartItemDTO> items = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        double grandTotal = items.stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();

        return new CartResponseDTO(cart.getId(), cart.getUser().getId(), items, grandTotal);
    }
    // Remove product from cart
//    public Cart removeFromCart(User user, Long productId) {
//        Cart cart = getCart(user);
//        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
//        return cartRepository.save(cart);
//    }
//
//    // Clear cart
//    public void clearCart(User user) {
//        Cart cart = getCart(user);
//        cart.getItems().clear();
//        cartRepository.save(cart);
//    }
}
