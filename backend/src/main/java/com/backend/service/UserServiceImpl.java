package com.backend.service;

import com.backend.errors.UserNotFoundException;
import com.backend.model.User;
import com.backend.model.enums.Role;
import com.backend.repository.UserRepository;
import com.backend.security.model.CustomUserDetails;
import com.backend.model.requests.RegisterRequest;
import com.backend.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    public List<User> getUsersByRole(final String ...roles) {
        return userRepository.findByRoleIn(Arrays.asList(roles));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

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

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public User updateUser(final Long id, final RegisterRequest registerRequest) {
        final User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        existingUser.setUsername(registerRequest.username());
        existingUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        existingUser.setRole(registerRequest.role());
        existingUser.setFirstName(registerRequest.firstName());
        existingUser.setSurname(registerRequest.surname());
        existingUser.setEmail(registerRequest.email());
        existingUser.setPhoneNumber(registerRequest.phoneNumber());
        return userRepository.save(existingUser);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @Override
    public void deleteUserById(final Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Auth -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

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
