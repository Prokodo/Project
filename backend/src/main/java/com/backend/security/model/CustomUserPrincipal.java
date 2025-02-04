package com.backend.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record CustomUserPrincipal(Long userId, String username, List<GrantedAuthority> authorities) {}
