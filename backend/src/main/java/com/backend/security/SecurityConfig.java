package com.backend.security;

import com.backend.security.utils.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;


    public SecurityConfig(final JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        http.cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                // Endpoints requiring authentication
                .requestMatchers("/api/requests/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/contracts/**", "/api/invoices/**", "/api/properties").authenticated()

                // Admin-only endpoints
                .requestMatchers("/api/users", "/api/properties/**").hasRole("ADMIN")
                .requestMatchers("/api/users/**", "/api/properties/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/contracts/**", "/api/invoices/**", "/api/properties").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/contracts/**", "/api/invoices/**", "/api/properties").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/contracts/**", "/api/invoices/**", "/api/properties").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
