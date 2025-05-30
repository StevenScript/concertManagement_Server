package com.example.concertManagement_Server.config;

import com.example.concertManagement_Server.ratelimit.LoginRateLimitFilter;
import com.example.concertManagement_Server.ratelimit.TicketsRateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private LoginRateLimitFilter loginRateLimitFilter;

    @Autowired
    private TicketsRateLimitFilter ticketsRateLimitFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS / CSRF
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // Rate-limit filters must come before any authentication
                .addFilterBefore(loginRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(ticketsRateLimitFilter, BasicAuthenticationFilter.class)

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Open POST endpoints for auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/login",
                                "/api/register",
                                "/api/refresh"
                        ).permitAll()

                        // Actuator & stats are public
                        .requestMatchers("/actuator/**", "/stats/**").permitAll()

                        // Public GETs for browsing
                        .requestMatchers(HttpMethod.GET,
                                "/venues/**",
                                "/artists/**",
                                "/events/**",
                                "/tickets/**",
                                "/users/**"
                        ).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // We'll use HTTP Basic for simplicity (or you can swap in JWT filters here)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Allow all origins + methods + credentials on our API.
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
