package com.backend.service;

import com.backend.model.Invoice;
import com.backend.repository.InvoiceRepository;
import com.backend.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(final long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID " + id));
    }

    public Invoice createInvoice(final Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(final long id, final Invoice updatedInvoice) {
        Invoice invoice = getInvoiceById(id);
        invoice.setContract(updatedInvoice.getContract());
        invoice.setIssueDate(updatedInvoice.getIssueDate());
        invoice.setDueDate(updatedInvoice.getDueDate());
        invoice.setAmount(updatedInvoice.getAmount());
        invoice.setPaid(updatedInvoice.isPaid());
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(final long id) {
        invoiceRepository.deleteById(id);
    }
}
