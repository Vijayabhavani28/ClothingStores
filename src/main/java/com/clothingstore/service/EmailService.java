package com.clothingstore.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.clothingstore.model.Order;
import com.clothingstore.util.EmailTemplateBuilder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateBuilder emailTemplateBuilder;

    public void sendOrderStatusUpdate(Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(order.getUser().getEmail());
            helper.setSubject("Order Update - #" + order.getId());
            helper.setText(emailTemplateBuilder.buildOrderEmail(order), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
