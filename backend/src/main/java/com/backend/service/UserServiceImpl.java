package com.backend.service;

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

    public void registerUser(final @NotNull RegisterRequest registerRequest) {
        registerUser(
            registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getFirstName(),
            registerRequest.getSurname(), registerRequest.getEmail(), registerRequest.getPhoneNumber(),
            registerRequest.getRole()
        );
    }

    public void registerUser(
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
        userRepository.save(new User(username, passwordEncoder.encode(password), firstName, surname, email, phoneNumber, role.toUpperCase()));
    }
}
