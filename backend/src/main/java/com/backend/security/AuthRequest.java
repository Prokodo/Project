package com.backend.security;

import org.jetbrains.annotations.NotNull;

public record AuthRequest(@NotNull String username, @NotNull String password) {
    public AuthRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public @NotNull String username() {
        return username;
    }

    @Override
    public @NotNull String password() {
        return password;
    }
}
