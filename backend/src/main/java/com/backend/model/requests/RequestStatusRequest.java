package com.backend.model.requests;

import com.backend.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestStatusRequest(
    @NotNull(message = "Status cannot be null")
    RequestStatus status
) {}