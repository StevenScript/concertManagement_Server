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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.concertManagement_Server.security.JwtAuthFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private LoginRateLimitFilter loginRateLimitFilter;

    @Autowired
    private TicketsRateLimitFilter ticketsRateLimitFilter;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .addFilterBefore(loginRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(ticketsRateLimitFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // ✅ hook in JWT filter

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/login",
                                "/api/register",
                                "/api/refresh"
                        ).permitAll()

                        .requestMatchers("/actuator/**", "/stats/**").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/venues/**",
                                "/artists/**",
                                "/events/**",
                                "/tickets/**",
                                "/users/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                );

        return http.build(); // ❌ No httpBasic() here anymore
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
