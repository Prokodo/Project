package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Column(nullable = false)
    @NotNull(message = "Monthly rent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly rent must be greater than 0")
    private Double monthlyRent;

    @ManyToOne(optional = false)
    @NotNull(message = "Tenant is required")
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @ManyToOne(optional = false)
    @NotNull(message = "Property is required")
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @JsonIgnore
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Invoice> invoices = new ArrayList<>();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- CONSTRUCTORS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Contract() {}

    public Contract(final Property property, final User tenant, final LocalDate startDate, final LocalDate endDate, final double monthlyRent) {
        this.property = property;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public Long getId() {
        return id;
    }

    public User getTenant() {
        return tenant;
    }

    public Property getProperty() {
        return property;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- SETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void setId(final Long id) {
        this.id = id;
    }

    public void setTenant(final User tenant) {
        this.tenant = tenant;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setMonthlyRent(final Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }
}
