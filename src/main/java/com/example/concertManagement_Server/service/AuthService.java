package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import com.example.concertManagement_Server.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration and authentication,
 * including password encoding and JWT issuance.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user and returns an AuthResponse with JWT.
     * @throws UsernameAlreadyExistsException if the username is taken
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        User u = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        userRepository.save(u);
        String token = jwtTokenProvider.generateToken(u.getUsername());

        return new AuthResponse(u.getUsername(), u.getEmail(), u.getRole(), token);
    }

    /**
     * Convenience overload using raw credentials.
     */
    public AuthResponse register(String username, String password, String email, String role) {
        return register(new RegisterRequest(username, email, password, role));
    }

    /**
     * Authenticates user credentials and returns JWT on success.
     * @throws InvalidCredentialsException if authentication fails
     */
    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid username or password")
                );

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(u.getUsername());
        return new AuthResponse(u.getUsername(), u.getEmail(), u.getRole(), token);
    }

    /**
     * Convenience overload using raw credentials.
     */
    public AuthResponse login(String username, String password) {
        return login(new LoginRequest(username, password));
    }
}