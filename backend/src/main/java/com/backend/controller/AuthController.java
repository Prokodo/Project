package com.backend.controller;

import com.backend.model.requests.AuthRequest;
import org.jetbrains.annotations.NotNull;
import com.backend.security.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(final AuthenticationManager authenticationManager, final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles() {
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

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping("/login")
    public ResponseEntity<?> login(final @NotNull@RequestBody AuthRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(Map.of("token", jwtTokenProvider.generateToken(authentication)));
    }
}
