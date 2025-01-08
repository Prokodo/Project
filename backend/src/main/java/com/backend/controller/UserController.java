package com.backend.controller;

import com.backend.errors.UserNotFoundException;
import com.backend.model.User;
import com.backend.model.requests.RegisterRequest;
import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- GET -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @GetMapping("/tenants")
    public ResponseEntity<? extends Object> getAllTenants() {
        return ResponseEntity.ok(userService.getUsersByRole("TENANT"));
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- POST -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @PostMapping("/tenants/register")
    public ResponseEntity<? extends Object> register(final @NotNull@RequestBody RegisterRequest registerRequest) {
        try {
            final User registeredUser = userService.registerUser(registerRequest);
            return ResponseEntity.ok(registeredUser);
        }
        catch (final IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", exception.getMessage()));
        }
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PUT -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    @PutMapping("/tenants/{id}")
    public ResponseEntity<? extends Object> updateUser(final @PathVariable Long id, final @RequestBody RegisterRequest registerRequest) {
        try {
            final User updatedUser = userService.updateUser(id, registerRequest);
            return ResponseEntity.ok(updatedUser);
        }
        catch (final UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
        }
        catch (final IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", exception.getMessage()));
        }
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- DELETE -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    @DeleteMapping("/tenants/{id}")
    public ResponseEntity<? extends Object> deleteUser(final @PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(Map.of(
                "message", "User deleted successfully"
            ));
        }
        catch (final UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", exception.getMessage()
            ));
        }
        catch (final Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "An error occurred while deleting the user"
            ));
        }
    }
}
