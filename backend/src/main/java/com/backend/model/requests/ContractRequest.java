package com.backend.model.requests;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ContractRequest(
    @NotNull(message = "Property ID is required")
    Long propertyId,

    @NotNull(message = "Tenant ID is required")
    Long tenantId,

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    LocalDate startDate,

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    LocalDate endDate,

    @NotNull(message = "Monthly rent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly rent must be greater than 0")
    Double monthlyRent
) {}
