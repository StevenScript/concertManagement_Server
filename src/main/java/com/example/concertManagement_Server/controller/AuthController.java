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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse resp = authService.register(request);
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse resp = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(resp);
    }
}
