package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Primary, DTO‐based register
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }
        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())               // ← correct email mapping
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepository.save(u);

        String token = UUID.randomUUID().toString();
        return new AuthResponse(u.getUsername(), u.getEmail(), u.getRole(), token);
    }

    // Overload so old tests calling register(a,b,c,d) still compile:
    public AuthResponse register(String username,
                                 String password,
                                 String email,
                                 String role) {
        // Note: test suite passed (username, password, email, role)
        RegisterRequest dto = new RegisterRequest(username, email, password, role);
        return register(dto);
    }

    // Primary, DTO‐based login
    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(u.getUsername(), u.getEmail(), u.getRole(), token);
    }

    // Overload so old tests calling login(a,b) still compile:
    public AuthResponse login(String username, String password) {
        return login(new LoginRequest(username, password));
    }
}