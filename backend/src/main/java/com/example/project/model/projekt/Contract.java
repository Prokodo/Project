package com.example.project.model.projekt;

import com.example.project.model.enums.ContractStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private @NotNull LocalDate startDate;
    private @NotNull LocalDate endDate;
    private @NotNull double rentAmount;

    @Enumerated(EnumType.STRING)
    private @NotNull ContractStatus status;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private @NotNull List<@NotNull Invoice> invoices;

    public Contract() {}

    public Contract(final @NotNull LocalDate startDate, final @NotNull LocalDate endDate, final @NotNull double rentAmount, final @NotNull ContractStatus status) {
        this.status = status;
        this.endDate = endDate;
        this.startDate = startDate;
        this.rentAmount = rentAmount;
        this.invoices = new ArrayList<>();
    }

    public @NotNull long getId() {
        return id;
    }

    public @NotNull double getRentAmount() {
        return rentAmount;
    }

    public @NotNull LocalDate getEndDate() {
        return endDate;
    }

    public @NotNull LocalDate getStartDate() {
        return startDate;
    }

    public @NotNull ContractStatus getStatus() {
        return status;
    }

    public @NotNull List<@NotNull Invoice> getInvoices() {
        return invoices;
    }

    public void setEndDate(final @NotNull LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(final @NotNull ContractStatus status) {
        this.status = status;
    }

    public void setRentAmount(final @NotNull double rentAmount) {
        this.rentAmount = rentAmount;
    }
}
