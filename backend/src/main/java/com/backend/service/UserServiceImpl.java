package com.backend.service;

import com.backend.errors.UserNotFoundException;
import com.backend.model.User;
import com.backend.model.enums.Role;
import com.backend.repository.UserRepository;
import com.backend.security.model.CustomUserDetails;
import com.backend.model.requests.RegisterRequest;
import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(final User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByRole(final String role) {
        return userRepository.findByRole(role);
    }

    public User updateUser(long id, RegisterRequest registerRequest) {
        // Find the user by ID
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Update fields based on the RegisterRequest
        existingUser.setUsername(registerRequest.username());
        existingUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        existingUser.setRole(registerRequest.role());
        existingUser.setFirstName(registerRequest.firstName());
        existingUser.setSurname(registerRequest.surname());
        existingUser.setEmail(registerRequest.email());
        existingUser.setPhoneNumber(registerRequest.phoneNumber());

        // Save the updated user to the database
        return userRepository.save(existingUser);
    }

    public User registerUser(final RegisterRequest registerRequest) {
        return registerUser(
            registerRequest.username(), registerRequest.password(), registerRequest.firstName(),
            registerRequest.surname(), registerRequest.email(), registerRequest.phoneNumber(),
            registerRequest.role()
        );
    }

    public User registerUser(
        final String username, final String password, final String firstName,
        final String surname, final String email, final String phoneNumber, final String role
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

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        final List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).toList();
        return new CustomUserDetails(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }
}
