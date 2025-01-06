package com.backend.controller;

import com.backend.model.Invoice;
import com.backend.model.requests.InvoiceRequest;
import com.backend.security.model.CustomUserPrincipal;
import com.backend.service.InvoiceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        final @NotNull Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof @NotNull CustomUserPrincipal principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final @NotNull Long userId = principal.userId();
        final boolean isAdmin = principal.authorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        final @NotNull List<@NotNull Invoice> invoices = isAdmin
                ? invoiceService.getAllInvoices()
                : invoiceService.getInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(final @RequestBody InvoiceRequest invoice) {
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

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}

