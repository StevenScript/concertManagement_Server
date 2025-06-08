/**
 * TraceLoggingFilter.java
 *
 * A Spring filter that pulls the current Micrometer trace ID into SLF4J's MDC.
 * This ensures every log line includes a trace ID for distributed tracing.
 *
 * Works closely with:
 * - Micrometer Tracer (Micrometer's tracing context)
 * - SecurityConfig.java (which registers this filter early in the chain)
 */
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

@Component
public class TraceLoggingFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public TraceLoggingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Extracts the trace ID from the current Micrometer span
     * and stores it in the SLF4J MDC so logs can include it.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the current filter chain
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        Span current = tracer.currentSpan();
        if (current != null && current.context() != null) {
            MDC.put("traceId", current.context().traceId());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }
}

