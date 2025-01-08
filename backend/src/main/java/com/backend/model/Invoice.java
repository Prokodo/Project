package com.backend.model;

import com.backend.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Contract contract;

    @Column(nullable = false)
    @NotNull(message = "Issue date must not be null")
    private LocalDate issueDate;

    @Column(nullable = false)
    @NotNull(message = "Due date must not be null")
    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;

    @Column(nullable = false)
    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be a positive value")
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Invoice status must not be null")
    private InvoiceStatus status;

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- CONSTRUCTORS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Invoice() {}

    public Invoice(final Contract contract, final LocalDate issueDate, final LocalDate dueDate, final InvoiceStatus status) {
        this.status = status;
        this.dueDate = dueDate;
        this.contract = contract;
        this.issueDate = issueDate;
        this.amount = contract.getMonthlyRent();
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Contract getContract() {
        return contract;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- SETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void setId(final Long id) {
        this.id = id;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setContract(final Contract contract) {
        this.contract = contract;
    }

    public void setStatus(final InvoiceStatus status) {
        this.status = status;
    }

    public void setIssueDate(final LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
