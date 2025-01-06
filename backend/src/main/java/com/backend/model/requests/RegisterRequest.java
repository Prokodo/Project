package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

public record RegisterRequest(
    @NotNull String username, @NotNull String password, @NotNull String firstName,
    @NotNull String surname, @NotNull String email, @NotNull String phoneNumber,
    @NotNull String role
) {}