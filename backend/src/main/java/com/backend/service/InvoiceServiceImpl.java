package com.backend.service;

import com.backend.model.Contract;
import com.backend.model.Invoice;
import org.springframework.stereotype.Service;
import com.backend.model.requests.InvoiceRequest;
import com.backend.repository.ContractRepository;
import com.backend.repository.InvoiceRepository;
import com.backend.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ContractRepository contractRepository) {
        this.invoiceRepository = invoiceRepository;
        this.contractRepository = contractRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByUserId(final Long userId) {
        return invoiceRepository.findInvoicesByContractTenantId(userId);
    }

    public Invoice getInvoiceById(final long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID " + id));
    }

    public Invoice createInvoice(final InvoiceRequest request) {
        Invoice invoice = new Invoice();

        Contract contract = contractRepository.findById(request.contractId()).orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + request.contractId()));
        invoice.setPaid(request.paid());
        invoice.setContract(contract);
        invoice.setDueDate(request.dueDate());
        invoice.setIssueDate(request.issueDate());
        invoice.setAmount(contract.getMonthlyRent());

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
