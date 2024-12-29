package com.backend.controller;

import com.backend.security.AuthRequest;
import com.backend.security.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final @NotNull JwtTokenProvider jwtTokenProvider;
    private final @NotNull AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(final @NotNull AuthenticationManager authenticationManager, final @NotNull JwtTokenProvider jwtTokenProvider) {
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

    @GetMapping("/has-authority")
    public ResponseEntity<?> hasAuthority(final @RequestParam("authority") String authority) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "User is not authenticated",
                "authorized", false,
                "roles", new ArrayList<>()
            ));
        }

        final List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        final boolean hasAuthority = roles.contains(authority);
        return ResponseEntity.ok(Map.of(
            "message", hasAuthority ? "User has the required authority" : "User does not have the required authority",
            "authorized", hasAuthority,
            "roles", roles
        ));
    }
}
