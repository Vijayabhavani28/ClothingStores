package com.clothingstore.controller;

import com.clothingstore.model.CartItem;
import com.clothingstore.model.Product;
import com.clothingstore.model.User;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.ProductRepository;
import com.clothingstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal User user, @RequestParam Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        CartItem cartItem = cartRepository.findByUser(user)
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        cartRepository.save(cartItem);
        return ResponseEntity.ok("Product added to cart");
    }

    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal User user) {
        return cartRepository.findByUser(user);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id, @AuthenticationPrincipal User user) {
        cartRepository.deleteById(id);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal User user) {
        cartRepository.deleteByUser(user);
        return ResponseEntity.ok("Cart cleared");
    }
}
