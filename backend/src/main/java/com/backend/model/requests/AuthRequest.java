package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

public record AuthRequest(@NotNull String username, @NotNull String password) {}
