package com.backend.security;

import com.backend.security.model.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private String getJwtFromHeader(final HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private String getJwtFromCookies(final HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromHeader(request);
        if (jwt == null) {
            jwt = getJwtFromCookies(request);
        }

        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
            final Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
            final String username = jwtTokenProvider.getUsernameFromJWT(jwt);
            final List<GrantedAuthority> authorities = jwtTokenProvider.getRolesFromJWT(jwt);
            final CustomUserPrincipal principal = new CustomUserPrincipal(userId, username, authorities);

            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

