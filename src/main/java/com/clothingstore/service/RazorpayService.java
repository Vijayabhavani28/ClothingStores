//package com.clothingstore.service;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.clothingstore.model.Order;
//import com.razorpay.RazorpayClient;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class RazorpayService {
//
//    @Value("${razorpay.key.id}")
//    private String razorpayKeyId;
//
//    @Value("${razorpay.key.secret}")
//    private String razorpayKeySecret;
//
//    public Order createOrder(double amount, String currency, String receipt) throws Exception {
//        RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
//
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", (int) (amount * 100)); // Amount in paise
//        orderRequest.put("currency", currency);
//        orderRequest.put("receipt", receipt);
//        orderRequest.put("payment_capture", 1); // Auto-capture
//
//        return client.orders.create(orderRequest);
//    }
//}
