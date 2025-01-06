package com.backend.repository;

import com.backend.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findInvoicesByContractTenantId(Long userId);
}