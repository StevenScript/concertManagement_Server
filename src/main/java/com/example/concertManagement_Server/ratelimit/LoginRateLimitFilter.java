package com.example.concertManagement_Server.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.*;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    // burst 5, refill 10 per minute
    private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(5, Refill.intervally(10, Duration.ofMinutes(1))))
            .build();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("/api/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod()));
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
            res.getWriter().write("Too many login attempts. Please wait and retry.");
        }
    }
}
