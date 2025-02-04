package com.backend.model.requests;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "Username is mandatory")
    String username,

    @NotBlank(message = "Password is required")
    String password
) {}
