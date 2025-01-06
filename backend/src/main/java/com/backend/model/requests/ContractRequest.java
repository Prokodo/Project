package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record ContractRequest(
    @NotNull Long propertyId, @NotNull Long tenantId,
    @NotNull LocalDate startDate, @NotNull LocalDate endDate,
    @NotNull Double monthlyRent
) {}
