package com.backend.model.requests;

import java.time.LocalDate;

public record ContractRequest(Long propertyId, Long tenantId, LocalDate startDate, LocalDate endDate, Double monthlyRent) {

}
