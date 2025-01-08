package com.backend.model.requests;

import com.backend.model.enums.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class InvoiceStatusRequest {
    @JsonProperty("status")
    @NotNull(message = "Payment status must not be null")
    private InvoiceStatus status;

    public InvoiceStatus getPaymentStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}
