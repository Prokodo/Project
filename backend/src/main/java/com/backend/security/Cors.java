package com.backend.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Cors {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(final @NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")               // Apply CORS rules to all API routes
                        .allowedOrigins("http://localhost:3000")        // Allow requests from frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow main HTTP methods
                        .allowedHeaders("*")                            // Allow all headers
                        .allowCredentials(true);                        // Allow cookies/auth headers
            }
        };
    }
}
