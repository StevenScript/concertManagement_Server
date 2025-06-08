/**
 * SecurityConfig.java
 *
 * This configuration class sets up the Spring Security filter chain for the application.
 * It handles:
 * - CORS and CSRF settings
 * - Logging each request’s trace ID via TraceLoggingFilter
 * - Rate limiting via LoginRateLimitFilter and TicketsRateLimitFilter
 * - JWT authentication using JwtAuthFilter
 * - Role-based access rules for all endpoints
 *
 * Works closely with:
 * - JwtAuthFilter.java (JWT validation)
 * - TraceLoggingFilter.java (MDC trace ID logging)
 * - LoginRateLimitFilter.java and TicketsRateLimitFilter.java (rate limiting filters)
 */
package com.example.concertManagement_Server.config;

import com.example.concertManagement_Server.ratelimit.LoginRateLimitFilter;
import com.example.concertManagement_Server.ratelimit.TicketsRateLimitFilter;
import com.example.concertManagement_Server.security.JwtAuthFilter;
import com.example.concertManagement_Server.config.TraceLoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private TraceLoggingFilter traceLoggingFilter;

    @Autowired
    private LoginRateLimitFilter loginRateLimitFilter;

    @Autowired
    private TicketsRateLimitFilter ticketsRateLimitFilter;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Configures the Spring Security filter chain:
     * - Disables CSRF for API usage
     * - Adds custom filters in the correct order
     * - Defines public vs protected endpoints
     *
     * @param http the HttpSecurity configuration
     * @return the built SecurityFilterChain
     * @throws Exception if any configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // Filter chain execution order matters
                .addFilterBefore(traceLoggingFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(loginRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(ticketsRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register", "/api/refresh").permitAll()
                        .requestMatchers("/actuator/**", "/stats/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/venues/**", "/artists/**", "/events/**", "/tickets/**", "/users/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean using BCrypt.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures global CORS settings.
     * Allows all origins and common headers/methods.
     *
     * @return the configured CorsFilter bean
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}


