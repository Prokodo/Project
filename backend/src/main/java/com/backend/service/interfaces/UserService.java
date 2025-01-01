package com.backend.service.interfaces;

import com.backend.model.User;
import com.backend.security.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    void save(User user);
    User findByUsername(String username);
    List<User> getUsersByRole(String role);
    void registerUser(RegisterRequest registerRequest);
    void registerUser(String username, String password, String firstName, String surname, String email, String phoneNumber, String role);
}