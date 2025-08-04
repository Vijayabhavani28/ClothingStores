package com.clothingstore.service;

import com.clothingstore.model.Order;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {

    public ByteArrayInputStream generateInvoice(Order order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);

        com.itextpdf.layout.Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));
        document.add(new Paragraph("Invoice for Order #" + order.getId()));
        document.add(new Paragraph("Customer: " + order.getUser().getName()));
        document.add(new Paragraph("Email: " + order.getUser().getEmail()));
        document.add(new Paragraph("Total: ₹" + order.getTotalPrice()));
        document.add(new Paragraph("Status: " + order.getStatus()));
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
