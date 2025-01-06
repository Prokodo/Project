package com.backend.security.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final @NotNull Long userId;

    public CustomUserDetails(final @NotNull Long userId, final @NotNull String username, final @NotNull String password, final @NotNull Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public @NotNull Long getUserId() {
        return userId;
    }
}