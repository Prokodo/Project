package com.backend.model;

import com.backend.model.enums.RequestStatus;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;  // Status: REQUESTED, IN_PROGRESS, COMPLETED, REJECTED

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column
    private LocalDate completionDate;

    public Request() {}

    public Request(final User tenant, final Property property, final String description, final RequestStatus status, final LocalDate requestDate) {
        this.status = status;
        this.property = property;
        this.description = description;
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
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

    public void setProperty(final @NotNull Property property) {
        this.property = property;
    }

    public void setStatus(final @NotNull RequestStatus status) {
        this.status = status;
    }

    public void setDescription(final @NotNull String description) {
        this.description = description;
    }

    public void setRequestDate(final @NotNull LocalDate requestDate) {
        this.requestDate = requestDate;
    }
}
