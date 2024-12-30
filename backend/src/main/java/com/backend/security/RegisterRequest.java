package com.backend.security;

import org.jetbrains.annotations.NotNull;

public record RegisterRequest(@NotNull String username, @NotNull String password, @NotNull String role) {
    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public @NotNull String getRole() {
        return role;
    }
}