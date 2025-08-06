package com.clothingstore.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clothingstore.dto.OrderResponseDTO;
import com.clothingstore.model.Order;
import com.clothingstore.model.Product;
import com.clothingstore.repository.ProductRepository;
import com.clothingstore.service.OrderService;
import com.clothingstore.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    // ================== PRODUCT MANAGEMENT ==================

//    // Add a product
//    @PostMapping("/products")
//    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//        return ResponseEntity.ok(productService.addProduct(product));
//    }
//
//    // Update a product
//    @PutMapping("/products/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        return ResponseEntity.ok(productService.updateProduct(id, product));
//    }
//
//    // Delete a product
//    @DeleteMapping("/products/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }

//    // Get all products
//    @GetMapping("/products")
//    public ResponseEntity<List<Order>> getAllProducts() {
//        return ResponseEntity.ok(orderService.getAllProducts());
//    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
    
 // ✅ 3. Add new product (Admin only)
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    // ✅ 4. Update product (Admin only)
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    product.setCategory(updatedProduct.getCategory());
                    product.setImageUrl(updatedProduct.getImageUrl());
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 5. Delete product (Admin only) → Best practice: Return No Content
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok(product); // return deleted product
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrdersForAdmin();
    }
    

    // Update order status
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}
