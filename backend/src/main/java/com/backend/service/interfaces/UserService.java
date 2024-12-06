package com.backend.service.interfaces;

import com.backend.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void save(User user);
}