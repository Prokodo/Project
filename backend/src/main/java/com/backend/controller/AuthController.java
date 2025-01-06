package com.backend.controller;

import com.backend.model.requests.AuthRequest;
import org.jetbrains.annotations.NotNull;
import com.backend.security.JwtTokenProvider;
import com.backend.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final @NotNull UserService userService;
    private final @NotNull JwtTokenProvider jwtTokenProvider;
    private final @NotNull AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(final @NotNull UserService userService, final @NotNull AuthenticationManager authenticationManager, final @NotNull JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(final @RequestBody AuthRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(Map.of("token", jwtTokenProvider.generateToken(authentication)));
    }

    @GetMapping("/roles")
    public ResponseEntity<?> isLoggedIn() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "roles", new ArrayList<>(),
                "loggedIn", false
            ));
        }

        final List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(Map.of(
            "loggedIn", true,
            "roles", roles
        ));
    }

    @GetMapping("/has-authority")
    public ResponseEntity<?> hasAuthority(final @RequestParam("authority") String authority) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "User is not authenticated",
                "roles", new ArrayList<>(),
                "authorized", false,
                "loggedIn", false
            ));
        }

        final List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        final boolean hasAuthority = roles.contains(authority);
        return ResponseEntity.ok(Map.of(
            "message", hasAuthority ? "User has the required authority" : "User does not have the required authority",
            "authorized", hasAuthority,
            "loggedIn", true,
            "roles", roles
        ));
    }
}
