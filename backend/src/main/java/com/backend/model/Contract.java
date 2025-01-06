package com.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private double monthlyRent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    public Contract() {}

    public Contract(Property property, User tenant, LocalDate startDate, LocalDate endDate, double monthlyRent) {
        this.property = property;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
    }

    public long getId() {
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

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setId(final long id) {
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

    public void setMonthlyRent(final double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }
}
