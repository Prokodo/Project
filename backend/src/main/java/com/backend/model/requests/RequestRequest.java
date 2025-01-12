package com.backend.model.requests;

import jakarta.validation.constraints.Size;

public record RequestRequest(
    @Size(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    String description
) {}