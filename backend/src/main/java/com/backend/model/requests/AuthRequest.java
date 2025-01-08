package com.backend.model.requests;

import org.jetbrains.annotations.NotNull;

public record AuthRequest(String username, String password) {}
