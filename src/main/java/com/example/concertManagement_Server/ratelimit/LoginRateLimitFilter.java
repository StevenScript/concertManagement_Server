package com.example.concertManagement_Server.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

/**
 * Global rate limiter for login requests to protect against brute-force attacks.
 *
 * - Applies only to POST /api/login requests.
 * - Uses a single global Bucket instance (shared across all users/IPs).
 * - Allows 5 requests instantly, refills 10 tokens per minute.
 *
 * Works closely with:
 * - AuthController (where /api/login is handled)
 * - Spring Security filter chain (runs before authentication logic)
 */
@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(5, Refill.intervally(10, Duration.ofMinutes(1))))
            .build();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("/api/login".equals(request.getRequestURI()) &&
                "POST".equalsIgnoreCase(request.getMethod()));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many login attempts. Please wait and try again.");
        }
    }
}
