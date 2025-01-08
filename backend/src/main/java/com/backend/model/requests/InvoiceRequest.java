package com.backend.model.requests;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record InvoiceRequest(
    @NotNull(message = "Contract ID must not be null")
    @Positive(message = "Contract ID must be a positive value")
    Long contractId,

    @NotNull(message = "Issue date must not be null")
    LocalDate issueDate,

    @NotNull(message = "Due date must not be null")
    @FutureOrPresent(message = "Due date must be in the present or future")
    LocalDate dueDate
) {}
