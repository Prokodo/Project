package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "(ADMIN|TENANT)", message = "Role must be either ADMIN or TENANT")
    private String role;

    @Column(nullable = false)
    @NotBlank(message = "First name is mandatory")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Surname is mandatory")
    @Size(max = 100, message = "Surname cannot exceed 100 characters")
    private String surname;

    @Column(nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Contract> contracts = new ArrayList<>();

    public User() {}

    public User(final String username, final String password, final String firstName, final String surname, final String email, final String phoneNumber, final String role) {
        this.role = role;
        this.email = email;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- SETTERS -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void setId(final long id) {
        this.id = id;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
