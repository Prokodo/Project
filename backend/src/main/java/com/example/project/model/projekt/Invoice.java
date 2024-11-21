package com.example.project.model.projekt;

import com.example.project.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private @NotNull InvoiceStatus status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private @NotNull Contract contract;

    private @NotNull double amountDue;
    private @NotNull double amountPaid;
    private @NotNull LocalDate dueDate;
    private @NotNull LocalDate issuedDate;

    public Invoice() {}

    public Invoice(final @NotNull LocalDate issuedDate, final @NotNull LocalDate dueDate, final @NotNull double amountDue, final @NotNull InvoiceStatus status) {
        this.status = status;
        this.dueDate = dueDate;
        this.amountDue = amountDue;
        this.issuedDate = issuedDate;
    }

    public @NotNull long getId() {
        return id;
    }

    public @NotNull double getAmountDue() {
        return amountDue;
    }

    public @NotNull LocalDate getDueDate() {
        return dueDate;
    }

    public @NotNull double getAmountPaid() {
        return amountPaid;
    }

    public @NotNull InvoiceStatus getStatus() {
        return status;
    }

    public @NotNull LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setId(final @NotNull long id) {
        this.id = id;
    }

    public void setDueDate(final @NotNull LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setAmountDue(final @NotNull double amountDue) {
        this.amountDue = amountDue;
    }

    public void setStatus(final @NotNull InvoiceStatus status) {
        this.status = status;
    }

    public void setAmountPaid(final @NotNull double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setIssuedDate(final @NotNull LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }
}
