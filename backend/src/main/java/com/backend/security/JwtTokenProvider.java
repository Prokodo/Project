package com.backend.security;

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
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(final @NotNull Authentication authentication) {
        final String username = authentication.getName();
        final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts
            .builder().setSubject(username).claim("roles", authorities).setIssuedAt(now)
            .setExpiration(expiryDate).signWith(signingKey, SignatureAlgorithm.HS512).compact();
    }

    public String getUsernameFromJWT(final @NotNull String token) {
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getRolesFromJWT(final @NotNull String token) {
        final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        final Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();

        final String roles = claims.get("roles", String.class);
        if (roles == null || roles.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public boolean validateToken(final @NotNull String authToken) {
        try {
            final Key signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(authToken);
            return true;
        } catch (final SignatureException exception) {
            System.out.println("Invalid JWT signature");
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
