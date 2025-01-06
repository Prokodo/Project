package com.backend.security;

import com.backend.security.model.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final @NotNull JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(final @NotNull JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private @Nullable String getJwtFromCookies(final @NotNull HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(final @NotNull HttpServletRequest request, final @NotNull HttpServletResponse response, final @NotNull FilterChain filterChain) throws ServletException, IOException {
        final @Nullable String jwt = getJwtFromCookies(request);
        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            final @NotNull Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
            final @NotNull String username = jwtTokenProvider.getUsernameFromJWT(jwt);
            final @NotNull List<GrantedAuthority> authorities = jwtTokenProvider.getRolesFromJWT(jwt);
            final @NotNull CustomUserPrincipal principal = new CustomUserPrincipal(userId, username, authorities);

            final @NotNull UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

