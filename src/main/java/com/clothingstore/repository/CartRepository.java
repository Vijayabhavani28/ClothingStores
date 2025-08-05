package com.clothingstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clothingstore.model.Cart;
import com.clothingstore.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}