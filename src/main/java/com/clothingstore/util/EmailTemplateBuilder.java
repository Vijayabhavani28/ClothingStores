package com.clothingstore.util;

import com.clothingstore.model.Order;
import com.clothingstore.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildOrderEmail(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Order Confirmation - #" + order.getId() + "</h2>");
        sb.append("<p>Hello, " + order.getUser().getName() + ",</p>");
        sb.append("<p>Your order has been placed successfully.</p>");
        sb.append("<table border='1' cellpadding='5' cellspacing='0'>");
        sb.append("<tr><th>Product</th><th>Quantity</th><th>Price</th></tr>");

        for (OrderItem item : order.getItems()) {
            sb.append("<tr>")
                    .append("<td>").append(item.getProduct().getName()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("<td>").append(item.getPrice() * item.getQuantity()).append("</td>")
                    .append("</tr>");
        }

        sb.append("</table>");
        sb.append("<p><strong>Total Price: $" + order.getTotalPrice() + "</strong></p>");
        sb.append("<p>You can download your invoice from the store dashboard.</p>");
        return sb.toString();
    }
}
