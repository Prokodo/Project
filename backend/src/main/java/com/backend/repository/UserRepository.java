package com.backend.repository;

import com.backend.model.Property;
import com.backend.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(String role);
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}