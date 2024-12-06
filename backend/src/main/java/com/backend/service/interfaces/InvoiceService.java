package com.backend.service.interfaces;

import com.backend.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    List<Invoice> getAllInvoices();
    Invoice getInvoiceById(long id);
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(long id, Invoice updatedInvoice);
    void deleteInvoice(long id);
}
