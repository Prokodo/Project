package com.backend.model.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 8 characters")
    String password,

    @NotBlank(message = "First name is mandatory")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    String firstName,

    @NotBlank(message = "Surname is mandatory")
    @Size(max = 100, message = "Surname cannot exceed 100 characters")
    String surname,

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    String email,

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number should be valid")
    String phoneNumber,

    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "(ADMIN|TENANT)", message = "Role must be either ADMIN or TENANT")
    String role
) {}