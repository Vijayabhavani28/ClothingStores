//package com.clothingstore.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.clothingstore.dto.CartItemDTO;
//import com.clothingstore.dto.CartResponseDTO;
//import com.clothingstore.model.Cart;
//import com.clothingstore.model.CartItem;
//import com.clothingstore.model.Product;
//import com.clothingstore.model.User;
//import com.clothingstore.repository.CartRepository;
//import com.clothingstore.repository.ProductRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class CartService {
//
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//
//    public CartResponseDTO addItemToCart(User user, Long productId, int quantity) {
//        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            return cartRepository.save(newCart);
//        });
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        CartItem existingItem = cart.getItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst()
//                .orElse(null);
//
//        if (existingItem != null) {
//            existingItem.setQuantity(existingItem.getQuantity() + quantity);
//        } else {
//            CartItem newItem = new CartItem();
//            newItem.setCart(cart);
//            newItem.setProduct(product);
//            newItem.setQuantity(quantity);
//            cart.getItems().add(newItem);
//        }
//
//        cartRepository.save(cart);
//        return buildCartResponse(cart);
//    }
//
//    public CartResponseDTO getCart(User user) {
//        Cart cart = cartRepository.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//        return buildCartResponse(cart);
//    }
//
//    private CartResponseDTO buildCartResponse(Cart cart) {
//        List<CartItemDTO> items = cart.getItems().stream()
//                .map(item -> new CartItemDTO(
//                        item.getProduct().getId(),
//                        item.getProduct().getName(),
//                        item.getProduct().getPrice(),
//                        item.getQuantity(),
//                        item.getProduct().getPrice() * item.getQuantity()
//                ))
//                .collect(Collectors.toList());
//
//        double grandTotal = items.stream()
//                .mapToDouble(CartItemDTO::getTotalPrice)
//                .sum();
//
//        return new CartResponseDTO(cart.getId(), cart.getUser().getId(), items, grandTotal);
//    }
//
//}


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

    // ✅ Helper: Get or create cart for the user
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // ✅ Add product to cart
    public CartResponseDTO addToCart(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
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
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return mapToCartResponseDTO(cart);
    }

    // ✅ Update cart item
    public CartResponseDTO updateCartItem(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        item.setQuantity(quantity);
        cartRepository.save(cart);
        return mapToCartResponseDTO(cart);
    }

    // ✅ Remove from cart
    public CartResponseDTO removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
        return mapToCartResponseDTO(cart);
    }

    // ✅ Clear cart
    public CartResponseDTO clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
        return mapToCartResponseDTO(cart);
    }

    // ✅ Get cart
    public CartResponseDTO getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return mapToCartResponseDTO(cart);
    }

    // ✅ Mapper: Convert cart to DTO
    private CartResponseDTO mapToCartResponseDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        double grandTotal = itemDTOs.stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();

        return new CartResponseDTO(cart.getId(), cart.getUser().getId(), itemDTOs, grandTotal);
    }
}

