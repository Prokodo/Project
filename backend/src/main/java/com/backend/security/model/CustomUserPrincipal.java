package com.backend.security.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record CustomUserPrincipal(@NotNull Long userId, @NotNull String username, @NotNull List<GrantedAuthority> authorities) {}
