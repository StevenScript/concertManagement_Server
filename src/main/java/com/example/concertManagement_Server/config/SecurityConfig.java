package com.example.concertManagement_Server.config;

import com.example.concertManagement_Server.ratelimit.LoginRateLimitFilter;
import com.example.concertManagement_Server.ratelimit.TicketsRateLimitFilter;
import com.example.concertManagement_Server.security.JwtAuthFilter;
import com.example.concertManagement_Server.config.TraceLoggingFilter;  // ← import your new filter
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) CORS / CSRF
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // 2) Trace → MDC (must be first so every subsequent filter/log has the traceId)
                .addFilterBefore(traceLoggingFilter, BasicAuthenticationFilter.class)

                // 3) Rate-limit checks
                .addFilterBefore(loginRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(ticketsRateLimitFilter, BasicAuthenticationFilter.class)

                // 4) JWT validation
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 5) Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Open endpoints for login/register/refresh
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register", "/api/refresh")
                        .permitAll()

                        // Allow unauthenticated access to actuator & landing stats
                        .requestMatchers("/actuator/**", "/stats/**")
                        .permitAll()

                        // Public GET browsing
                        .requestMatchers(HttpMethod.GET,
                                "/venues/**",
                                "/artists/**",
                                "/events/**",
                                "/tickets/**",
                                "/users/**"
                        ).permitAll()

                        // Everything else requires JWT auth
                        .anyRequest().authenticated()
                );

        // no httpBasic() here— we're using JWT exclusively
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

