//package com.clothingstore.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.clothingstore.dto.OrderItemDTO;
//import com.clothingstore.dto.OrderResponseDTO;
//import com.clothingstore.model.Cart;
//import com.clothingstore.model.CartItem;
//import com.clothingstore.model.Order;
//import com.clothingstore.model.OrderItem;
//import com.clothingstore.model.User;
//import com.clothingstore.repository.CartRepository;
//import com.clothingstore.repository.OrderRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//
//@Service
////@RequiredArgsConstructor
//public class OrderService {
//
//    private final CartRepository cartRepository;
//    private final OrderRepository orderRepository;
//    private final EmailService emailService; // Added email service
//    @Transactional
//    public OrderResponseDTO placeOrder(User user) {
//        Cart cart = cartRepository.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        if (cart.getItems().isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//
//        double totalPrice = cart.getItems().stream()
//                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
//                .sum();
//
//        // Create order
//        Order order = new Order();
//        order.setUser(user);
//        order.setStatus("PENDING");
//        order.setItems(new ArrayList<>());
//
//        for (CartItem cartItem : cart.getItems()) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(cartItem.getProduct());
//            orderItem.setQuantity(cartItem.getQuantity());
//            orderItem.setPrice(cartItem.getProduct().getPrice());
//            order.getItems().add(orderItem);
//        }
//
//        order.setTotalPrice(totalPrice);
//        orderRepository.save(order);
//
//        // Clear cart
//        cart.getItems().clear();
//        cartRepository.save(cart);
//        
//
//        // Send email notification
//        emailService.sendOrderStatusUpdate(order);
//        
//        
//
//        // Build response
////        List<OrderResponseDTO.ProductInfo> productInfoList = order.getItems().stream()
////                .map(item -> new OrderResponseDTO.ProductInfo(
////                        item.getProduct().getName(),
////                        item.getPrice(),
////                        item.getQuantity()
////                ))
////                .collect(Collectors.toList());
////
////        return new OrderResponseDTO(order.getId(), productInfoList, totalPrice, order.getStatus());
////    }
//
//
//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }
//    public List<OrderResponseDTO> getUserOrders(User user) {
//      List<Order> orders = orderRepository.findByUser(user);
//      return orders.stream()
//              .map(this::convertToDTO)
//              .collect(Collectors.toList());
//  }
//
//  public List<OrderResponseDTO> getAllOrdersForAdmin() {
//      List<Order> orders = orderRepository.findAll();
//      return orders.stream()
//              .map(this::convertToDTO)
//              .collect(Collectors.toList());
//  }
//
//  private OrderResponseDTO convertToDTO(Order order) {
//      List<OrderItemDTO> items = order.getItems().stream()
//              .map(item -> new OrderItemDTO(
//                      item.getProduct().getId(),
//                      item.getProduct().getName(),
//                      item.getPrice(),
//                      item.getQuantity(),
//                      item.getPrice() * item.getQuantity()
//              ))
//              .collect(Collectors.toList());
//
//      return new OrderResponseDTO(order.getId(), items, order.getTotalPrice(), order.getStatus());
//  }
//
//
//    public Order updateOrderStatus(Long orderId, String status) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//        order.setStatus(status);
//        orderRepository.save(order);
//
//        // Send email notification on status update
//        emailService.sendOrderStatusUpdate(order);
//
//        return order;
//    }
//}
//
//


package com.clothingstore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clothingstore.dto.OrderItemDTO;
import com.clothingstore.dto.OrderResponseDTO;
import com.clothingstore.model.Cart;
import com.clothingstore.model.CartItem;
import com.clothingstore.model.Order;
import com.clothingstore.model.OrderItem;
import com.clothingstore.model.User;
import com.clothingstore.repository.CartRepository;
import com.clothingstore.repository.OrderRepository;

//import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final EmailService emailService;

    /**
     * Place an order for the user.
     */
    @Transactional
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
//
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Clear cart after placing order
        cart.getItems().clear();
        cartRepository.save(cart);
        

//      // Send email notification
      emailService.sendOrderStatusUpdate(order);

        return convertToDTO(order);
    }

    /**
     * Get all orders for the logged-in user.
     */
    public List<OrderResponseDTO> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all orders for admin.
     */
    public List<OrderResponseDTO> getAllOrdersForAdmin() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Order entity to DTO.
     */
    private OrderResponseDTO convertToDTO(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(order.getId(), items, order.getTotalPrice(), order.getStatus());
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

