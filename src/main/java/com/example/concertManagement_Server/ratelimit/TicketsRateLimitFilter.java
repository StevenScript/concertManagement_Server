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
 * Rate limiter for ticket purchase requests to prevent abuse or automated spamming.
 *
 * - Applies only to POST /tickets requests.
 * - Uses a single global Bucket instance (shared across all users/IPs).
 * - Allows 20 quick bursts, then refills 5 tokens per second.
 *
 * Works closely with:
 * - TicketController (handles /tickets endpoint)
 * - TicketService (business logic for ticket creation)
 */
@Component
public class TicketsRateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(20, Refill.intervally(5, Duration.ofSeconds(1))))
            .build();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/tickets") ||
                !"POST".equalsIgnoreCase(request.getMethod());
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
            response.getWriter().write("Too many ticket requests. Please slow down.");
        }
    }
}
