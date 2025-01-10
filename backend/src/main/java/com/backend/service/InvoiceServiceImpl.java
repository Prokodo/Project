package com.backend.service;

import com.backend.model.Contract;
import com.backend.model.Invoice;
import com.backend.model.enums.InvoiceStatus;
import org.springframework.stereotype.Service;
import com.backend.model.requests.InvoiceRequest;
import com.backend.repository.ContractRepository;
import com.backend.repository.InvoiceRepository;
import com.backend.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public InvoiceServiceImpl(final InvoiceRepository invoiceRepository, final ContractRepository contractRepository) {
        this.invoiceRepository = invoiceRepository;
        this.contractRepository = contractRepository;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public List<Invoice> getListOfInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByUserId(final Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        return invoiceRepository.findInvoicesByContractTenantId(userId);
    }

    public Optional<Invoice> getInvoiceById(final Long invoiceId) {
        if (invoiceId == null || invoiceId <= 0) {
            throw new IllegalArgumentException("Invalid invoice ID.");
        }
        return invoiceRepository.findById(invoiceId);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Invoice createInvoice(final InvoiceRequest request) {
        if (request.contractId() == null || request.contractId() <= 0) {
            throw new IllegalArgumentException("Invalid contract ID in invoice request.");
        }
        if (request.dueDate() == null || request.issueDate() == null) {
            throw new IllegalArgumentException("Due date and issue date cannot be null.");
        }
        if (request.dueDate().isBefore(request.issueDate())) {
            throw new IllegalArgumentException("Due date cannot be earlier than issue date.");
        }

        final Contract contract = contractRepository.findById(request.contractId()).orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + request.contractId()));
        final Invoice invoice = new Invoice(contract, request.issueDate(), request.dueDate(), InvoiceStatus.UNPAID);
        return invoiceRepository.save(invoice);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Invoice updateInvoicePaidStatus(final Long id, final InvoiceStatus status) {
        final Invoice invoice = getInvoiceById(id).orElseThrow(() -> new RuntimeException("Invoice with ID " + id + " not found."));
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
