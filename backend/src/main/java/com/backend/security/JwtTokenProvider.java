package com.backend.security;

import com.backend.security.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
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
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationInMs;

    public String generateToken(final Authentication authentication) {
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        final Long userId = userDetails.getUserId();
        final String username = userDetails.getUsername();
        final String authorities = userDetails.getAuthorities().stream().map(authority -> {
            final String role = authority.getAuthority();
            return role.startsWith("ROLE_") ? role : "ROLE_" + role;
        }).collect(Collectors.joining(","));

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().setSubject(username).claim("userId", userId) .claim("roles", authorities)
            .setIssuedAt(now).setExpiration(expiryDate).signWith(signingKey, SignatureAlgorithm.HS512).compact();
    }

    public Long getUserIdFromJWT(final String token) {
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        final Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }

    public String getUsernameFromJWT(final String token) {
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getRolesFromJWT(final String token) {
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        final Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();

        final String roles = claims.get("roles", String.class);
        if (roles == null || roles.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Boolean validateToken(final String authToken) {
        try {
            final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(authToken);
            return true;
        } catch (final SecurityException exception) {
            System.out.println("Invalid JWT signature or security issue");
        } catch (final MalformedJwtException exception) {
            System.out.println("Invalid JWT token");
        } catch (final ExpiredJwtException exception) {
            System.out.println("Expired JWT token");
        } catch (final UnsupportedJwtException exception) {
            System.out.println("Unsupported JWT token");
        } catch (final IllegalArgumentException exception) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}
