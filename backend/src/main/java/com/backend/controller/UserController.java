package com.backend.controller;

import com.backend.model.User;
import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  /*  private final UserService userService;

    @Autowired
    public UserController(final @NotNull UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getAllProperties() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getPropertyById(final @PathVariable long id) {
        return userService
            .getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public User createProperty(final @RequestBody User user) {
        return userService.saveProperty(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(final @PathVariable long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteProperty(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateProperty(final @PathVariable long id, final @RequestBody User updatedProperty) {
        try {
            return ResponseEntity.ok(userService.updateProperty(id, updatedProperty));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }*/
}
