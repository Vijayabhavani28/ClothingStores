package com.clothingstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clothingstore.dto.CartResponseDTO;
import com.clothingstore.model.Cart;
import com.clothingstore.model.User;
import com.clothingstore.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public CartResponseDTO addItemToCart(@AuthenticationPrincipal User user,
                                         @RequestParam Long productId,
                                         @RequestParam int quantity) {
        return cartService.addItemToCart(user, productId, quantity);
    }

    @GetMapping
    public CartResponseDTO getCart(@AuthenticationPrincipal User user) {
        return cartService.getCart(user);
    }
    // Remove product from cart
//    @DeleteMapping("/remove")
//    public ResponseEntity<Cart> removeFromCart(
//            @AuthenticationPrincipal User user,
//            @RequestParam Long productId) {
//        return ResponseEntity.ok(cartService.removeFromCart(user, productId));
//    }
//
//    // Clear the cart
//    @DeleteMapping("/clear")
//    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
//        cartService.clearCart(user);
//        return ResponseEntity.noContent().build();
//    }
}
