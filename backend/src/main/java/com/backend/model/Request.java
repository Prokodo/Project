package com.backend.model;

import com.backend.model.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private RequestStatus status;

    @Column(nullable = false)
    @PastOrPresent(message = "Request date must be in the past or present")
    private LocalDate requestDate;

    @Column(nullable = true)
    @PastOrPresent(message = "Completion date must be in the past or present")
    private LocalDate completionDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User tenant;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Request() {}

    public Request(final User tenant, final Property property, final String description, final RequestStatus status, final LocalDate requestDate) {
        this.tenant = tenant;
        this.status = status;
        this.property = property;
        this.description = description;
        this.requestDate = requestDate;
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

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- SETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void setId(final long id) {
        this.id = id;
    }

    public void setStatus(final RequestStatus status) {
        this.status = status;
        if (status == RequestStatus.COMPLETED) {
            this.completionDate = LocalDate.now();
        } else {
            this.completionDate = null;
        }
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
