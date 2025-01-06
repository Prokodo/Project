package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record InvoiceRequest(
    @NotNull Long contractId,
    @NotNull LocalDate issueDate, @NotNull LocalDate dueDate,
    @NotNull Boolean paid
) {}
