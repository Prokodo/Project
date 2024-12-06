package com.backend.controller;

import com.backend.model.Invoice;
import com.backend.service.InvoiceServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final @NotNull InvoiceServiceImpl invoiceService;

    @Autowired
    public InvoiceController(final @NotNull InvoiceServiceImpl invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(final @PathVariable long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(final @RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(final @PathVariable long id, final @RequestBody Invoice updatedInvoice) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, updatedInvoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(final @PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}

