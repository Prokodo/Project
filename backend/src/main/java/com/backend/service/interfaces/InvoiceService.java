package com.backend.service.interfaces;

import com.backend.model.Invoice;
import com.backend.model.requests.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    List<Invoice> getAllInvoices();
    Invoice getInvoiceById(long id);
    List<Invoice> getInvoicesByUserId(Long userId);
    Invoice createInvoice(InvoiceRequest invoice);
    Invoice updateInvoice(long id, Invoice updatedInvoice);
    void deleteInvoice(long id);
}
