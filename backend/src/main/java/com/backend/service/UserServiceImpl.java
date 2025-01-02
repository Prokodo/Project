package com.backend.service;

import com.backend.errors.UserNotFoundException;
import com.backend.model.User;
import com.backend.model.enums.Role;
import com.backend.repository.UserRepository;
import com.backend.security.RegisterRequest;
import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final @NotNull UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final @NotNull UserRepository userRepository, final @NotNull PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(final @NotNull User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(final @NotNull String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByRole(final @NotNull String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public UserDetails loadUserByUsername(final @NotNull String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }

    public User updateUser(long id, RegisterRequest registerRequest) {
        // Find the user by ID
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Update fields based on the RegisterRequest
        existingUser.setUsername(registerRequest.getUsername());
        existingUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        existingUser.setRole(registerRequest.getRole());
        existingUser.setFirstName(registerRequest.getFirstName());
        existingUser.setSurname(registerRequest.getSurname());
        existingUser.setEmail(registerRequest.getEmail());
        existingUser.setPhoneNumber(registerRequest.getPhoneNumber());

        // Save the updated user to the database
        return userRepository.save(existingUser);
    }

    public User registerUser(final @NotNull RegisterRequest registerRequest) {
        return registerUser(
            registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getFirstName(),
            registerRequest.getSurname(), registerRequest.getEmail(), registerRequest.getPhoneNumber(),
            registerRequest.getRole()
        );
    }

    public User registerUser(
        final @NotNull String username, final @NotNull String password, final @NotNull String firstName,
        final @NotNull String surname, final @NotNull String email, final @NotNull String phoneNumber, final @NotNull String role
    ) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (Stream.of(Role.values()).noneMatch(r -> r.name().equalsIgnoreCase(role))) {
            final String allowedRoles = Stream.of(Role.values()).map(Enum::name).collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid role. Allowed roles are: " + allowedRoles);
        }

        final User user = new User(username, passwordEncoder.encode(password), firstName, surname, email, phoneNumber, role.toUpperCase());
        userRepository.save(user);
        return user;
    }
}
