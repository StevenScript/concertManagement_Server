package com.example.concertManagement_Server.config;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Pulls the current micrometer‐tracing span's trace ID
 * into SLF4J's MDC so that every log can include it.
 */
@Component
public class TraceLoggingFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public TraceLoggingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        Span current = tracer.currentSpan();
        if (current != null && current.context() != null) {
            String traceId = current.context().traceId();
            MDC.put("traceId", traceId);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }
}

