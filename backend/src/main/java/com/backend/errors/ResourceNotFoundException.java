package com.backend.errors;

import org.jetbrains.annotations.NotNull;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
