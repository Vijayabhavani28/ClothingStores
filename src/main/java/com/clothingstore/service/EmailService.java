package com.clothingstore.service;

import java.io.ByteArrayInputStream;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.clothingstore.util.EmailTemplateBuilder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateBuilder templateBuilder;

    public void sendInvoiceEmail(String to, String name, Long orderId, double total, ByteArrayInputStream pdfInvoice) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Order #" + orderId + " Invoice");
        helper.setText(templateBuilder.buildInvoiceEmail(name, orderId, total), true);

        helper.addAttachment("invoice_" + orderId + ".pdf", (InputStreamSource) () -> pdfInvoice);

        mailSender.send(message);
    }

    public void sendOrderStatusUpdate(String to, String name, Long orderId, String status) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Order #" + orderId + " Status Update");
        helper.setText(templateBuilder.buildStatusUpdateEmail(name, orderId, status), true);

        mailSender.send(message);
    }
}
