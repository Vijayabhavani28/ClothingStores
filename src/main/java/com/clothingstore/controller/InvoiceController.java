package com.clothingstore.controller;

import com.clothingstore.model.Order;
import com.clothingstore.repository.OrderRepository;
import com.clothingstore.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final OrderRepository orderRepository;
    private final InvoiceService invoiceService;

    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow();
        ByteArrayInputStream invoicePdf = invoiceService.generateInvoice(order);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(invoicePdf.readAllBytes());
    }
}
