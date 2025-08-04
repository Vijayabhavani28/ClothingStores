package com.clothingstore.util;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildInvoiceEmail(String name, Long orderId, double total) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color:#2b2b2b;'>Thank you for your order, " + name + "!</h2>" +
                "<p>Your order <strong>#" + orderId + "</strong> has been successfully placed.</p>" +
                "<p>Total Amount: <strong>₹" + total + "</strong></p>" +
                "<p>Your invoice is attached with this email.</p>" +
                "<p style='color:gray;'>Thank you for shopping with ClothingStore!</p>" +
                "</body></html>";
    }

    public String buildStatusUpdateEmail(String name, Long orderId, String status) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color:#2b2b2b;'>Hello " + name + ",</h2>" +
                "<p>Your order <strong>#" + orderId + "</strong> status has been updated to:</p>" +
                "<h3 style='color:green;'>" + status + "</h3>" +
                "<p>Thank you for shopping with ClothingStore!</p>" +
                "</body></html>";
    }
}
