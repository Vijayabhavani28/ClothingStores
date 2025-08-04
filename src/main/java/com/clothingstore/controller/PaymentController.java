//package com.clothingstore.controller;
//
//import com.clothingstore.service.RazorpayService;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONObject;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/payment")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final RazorpayService razorpayService;
//
//    @PostMapping("/create-order")
//    public ResponseEntity<?> createPaymentOrder(@RequestParam double amount) throws Exception {
//        JSONObject order = razorpayService.createRazorpayOrder(amount);
//        return ResponseEntity.ok(order.toString());
//    }
//}
