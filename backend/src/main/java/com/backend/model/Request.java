package com.backend.model;

import com.backend.model.enums.RequestStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Property property;

    @ManyToOne(optional = false)
    private User tenant;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status; // Status: REQUESTED, IN_PROGRESS, COMPLETED, REJECTED

    @Column(nullable = false)
    private LocalDate requestDate;

    public Request() {}

    public Request(Property property, User tenant, String description, RequestStatus status, LocalDate requestDate) {
        this.property = property;
        this.tenant = tenant;
        this.description = description;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
    }

    public User getTenant() {
        return tenant;
    }

    public Property getProperty() {
        return property;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setTenant(final User tenant) {
        this.tenant = tenant;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public void setStatus(final RequestStatus status) {
        this.status = status;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setRequestDate(final LocalDate requestDate) {
        this.requestDate = requestDate;
    }
}
