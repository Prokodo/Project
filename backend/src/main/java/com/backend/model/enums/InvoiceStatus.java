package com.backend.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum InvoiceStatus {
    PAID,
    UNPAID,
    PENDING,
    OVERDUE,
    CANCELLED,
}
