package com.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Contract contract;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private boolean paid;


    public Invoice() {}

    public Invoice(Contract contract, LocalDate issueDate, LocalDate dueDate, double amount, boolean paid) {
        this.contract = contract;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paid = paid;
    }

    public Long getId() {
        return id;
    }

    public boolean isPaid() {
        return paid;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Contract getContract() {
        return contract;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setPaid(final boolean paid) {
        this.paid = paid;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setContract(final Contract contract) {
        this.contract = contract;
    }

    public void setIssueDate(final LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
