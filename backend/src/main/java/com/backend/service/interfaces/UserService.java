package com.backend.service.interfaces;

import com.backend.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    void save(User user);
    User findByUsername(String username);
    void registerUser(String username, String password, String role);
}