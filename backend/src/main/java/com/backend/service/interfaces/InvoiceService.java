package com.backend.service.interfaces;

import com.backend.model.Invoice;
import com.backend.model.enums.InvoiceStatus;
import com.backend.model.requests.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    List<Invoice> getAllInvoices();
    Invoice getInvoiceById(Long id);
    List<Invoice> getInvoicesByUserId(Long userId);

    Invoice createInvoice(InvoiceRequest invoice);
    Invoice updateInvoicePaidStatus(Long id, InvoiceStatus status);
}
