package com.example.concertManagement_Server.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.io.IOException;
import java.time.Duration;

@Component
public class TicketsRateLimitFilter extends OncePerRequestFilter {

    // burst of 20 tokens, refilled by 5 tokens every second
    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(
                    20,
                    Refill.intervally(5, Duration.ofSeconds(1))
            ))
            .build();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/tickets")
                || !"POST".equals(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.getWriter().write("Too many ticket requests. Please slow down.");
        }
    }
}
