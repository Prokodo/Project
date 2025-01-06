package com.backend.security;

import com.backend.security.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private @NotNull String jwtSecret;

    @Value("${jwt.expiration}")
    private @NotNull Long jwtExpirationInMs;

    public String generateToken(final @NotNull Authentication authentication) {
        final @NotNull CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        final @NotNull Long userId = userDetails.getUserId();
        final @NotNull String username = userDetails.getUsername();
        final @NotNull String authorities = userDetails.getAuthorities().stream().map(authority -> {
            final @NotNull String role = authority.getAuthority();
            return role.startsWith("ROLE_") ? role : "ROLE_" + role;
        }).collect(Collectors.joining(","));

        final @NotNull Date now = new Date();
        final @NotNull Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        final @NotNull Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().setSubject(username).claim("userId", userId) .claim("roles", authorities)
            .setIssuedAt(now).setExpiration(expiryDate).signWith(signingKey, SignatureAlgorithm.HS512).compact();
    }

    public @NotNull Long getUserIdFromJWT(final @NotNull String token) {
        final @NotNull Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        final @NotNull Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }

    public @NotNull String getUsernameFromJWT(final @NotNull String token) {
        final @NotNull Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public @NotNull List<@NotNull GrantedAuthority> getRolesFromJWT(final @NotNull String token) {
        final @NotNull Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        final @NotNull Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();

        final @NotNull String roles = claims.get("roles", String.class);
        if (roles == null || roles.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public @NotNull Boolean validateToken(final @NotNull String authToken) {
        try {
            final @NotNull Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(authToken);
            return true;
        } catch (final @NotNull SecurityException exception) {
            System.out.println("Invalid JWT signature or security issue");
        } catch (final @NotNull MalformedJwtException exception) {
            System.out.println("Invalid JWT token");
        } catch (final @NotNull ExpiredJwtException exception) {
            System.out.println("Expired JWT token");
        } catch (final @NotNull UnsupportedJwtException exception) {
            System.out.println("Unsupported JWT token");
        } catch (final @NotNull IllegalArgumentException exception) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}
