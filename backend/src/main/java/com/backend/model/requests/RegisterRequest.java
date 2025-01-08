package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

public record RegisterRequest(
    String username, String password, String firstName,
    String surname, String email, String phoneNumber,
    String role
) {}