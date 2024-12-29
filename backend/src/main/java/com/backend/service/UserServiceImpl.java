package com.backend.service;

import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.service.interfaces.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final @NotNull UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final @NotNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(final @NotNull User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(final @NotNull String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(final @NotNull String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}
