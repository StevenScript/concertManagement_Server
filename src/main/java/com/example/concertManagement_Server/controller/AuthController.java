package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
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
     * Registers a new user and returns an AuthResponse containing:
     * username, email, role, JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse resp = authService.register(request);
        // 201 Created is semantically correct; adjust if you prefer 200
        return ResponseEntity.status(201).body(resp);
    }

    /**
     * Authenticates a user and returns AuthResponse
     * { username, email, role, token }.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }
}