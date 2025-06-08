/**
 * AuthController.java
 *
 * Handles authentication-related endpoints for user registration, login, and token refreshing.
 * Acts as the public gateway for creating JWTs and rotating refresh tokens.
 *
 * Works closely with:
 * - AuthService.java (business logic for registration and token management)
 * - RegisterRequest / LoginRequest / RefreshTokenRequest (request DTOs)
 * - AuthResponse (response wrapper containing tokens)
 */
package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.dto.RefreshTokenRequest;
import com.example.concertManagement_Server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user account and returns an AuthResponse with tokens.
     *
     * @param request the registration input (email, password, etc.)
     * @return 201 Created with access and refresh tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse resp = authService.register(request);
        return ResponseEntity.status(201).body(resp);
    }

    /**
     * Authenticates a user and returns a fresh AuthResponse with JWT tokens.
     *
     * @param request the login input (email and password)
     * @return 200 OK with access and refresh tokens if valid
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }

    /**
     * Accepts a refresh token and returns new access/refresh tokens.
     *
     * @param request the refresh token input
     * @return 200 OK with new tokens if the refresh token is valid
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse resp = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(resp);
    }
}
