package com.backend.security;

import com.backend.security.model.CustomUserPrincipal;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static boolean isAdmin(final @NotNull CustomUserPrincipal principal) {
        return principal.authorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));
    }

    public static @NotNull CustomUserPrincipal getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new SecurityException("Unauthorized access - no valid user found.");
        }
        return principal;
    }
}
