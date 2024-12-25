package com.backend.security;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(final @NotNull Authentication authentication) {
        final String username = authentication.getName();
        final String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .setSubject(username).claim("roles", authorities)
            .setIssuedAt(now).setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getUsernameFromJWT(final @NotNull String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(final @NotNull String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
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
