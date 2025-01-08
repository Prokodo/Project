package com.backend.controller;

import com.backend.model.Invoice;
import com.backend.model.requests.InvoiceRequest;
import com.backend.model.requests.InvoiceStatusRequest;
import com.backend.security.SecurityUtils;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.InvoiceServiceImpl;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceServiceImpl invoiceService;

    @Autowired
    public InvoiceController(final InvoiceServiceImpl invoiceService) {
        this.invoiceService = invoiceService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping
    public List<Invoice> getAllInvoices() {
        final @NotNull CustomUserPrincipal user = SecurityUtils.getCurrentUser();
        if (SecurityUtils.isAdmin(user)) {
            return invoiceService.getAllInvoices();
        }
        return invoiceService.getInvoicesByUserId(user.userId());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(final @RequestBody @Valid InvoiceRequest invoiceRequest) {
        final Invoice invoice = invoiceService.createInvoice(invoiceRequest);
        return ResponseEntity.ok(invoice);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/invoice-status/{id}")
    public ResponseEntity<Invoice> updateInvoice(final @NotNull@PathVariable Long id, final @RequestBody @Valid InvoiceStatusRequest statusRequest) {
        final Invoice updatedInvoice = invoiceService.updateInvoicePaidStatus(id, statusRequest.getPaymentStatus());
        return ResponseEntity.ok(updatedInvoice);
    }
}

