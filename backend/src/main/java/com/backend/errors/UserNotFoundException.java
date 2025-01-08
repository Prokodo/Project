package com.backend.errors;

import org.jetbrains.annotations.NotNull;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}