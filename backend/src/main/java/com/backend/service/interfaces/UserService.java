package com.backend.service.interfaces;

import com.backend.model.User;
import com.backend.model.requests.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    void save(User user);
    void deleteUserById(Long id);
    List<User> getUsersByRole(String role);
    User registerUser(RegisterRequest registerRequest);
    User updateUser(long id, RegisterRequest registerRequest);
    User registerUser(String username, String password, String firstName, String surname, String email, String phoneNumber, String role);
}