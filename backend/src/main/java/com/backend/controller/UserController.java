package com.backend.controller;

import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final @NotNull UserService userService;

    @Autowired
    public UserController(final @NotNull UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/tenants")
    public ResponseEntity<?> getAllTenants() {
        return ResponseEntity.ok(userService.getUsersByRole("TENANT"));
    }
}
