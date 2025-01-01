package com.backend.security;

import org.jetbrains.annotations.NotNull;

public record RegisterRequest(
        @NotNull String username, @NotNull String password, @NotNull String firstName,
        @NotNull String surname, @NotNull String email, @NotNull String phoneNumber,
        @NotNull String role
) {
    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public @NotNull String getRole() {
        return role;
    }

    public @NotNull String getSurname() {
        return surname;
    }

    public @NotNull String getEmail() {
        return email;
    }
}