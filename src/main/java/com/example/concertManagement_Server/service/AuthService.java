package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.exception.TokenRefreshException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import com.example.concertManagement_Server.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

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

        String accessToken = jwtTokenProvider.generateToken(u.getUsername());
        var refreshToken = refreshTokenService.createRefreshToken(u.getId()).getToken();

        return new AuthResponse(u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                accessToken,
                refreshToken);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtTokenProvider.generateToken(u.getUsername());
        var refreshToken = refreshTokenService.createRefreshToken(u.getId()).getToken();

        return new AuthResponse(u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                accessToken,
                refreshToken);
    }

    public AuthResponse refreshToken(String requestToken) {
        RefreshTokenService svc = this.refreshTokenService;
        var tokenEntity = svc.verifyExpiration(
                refreshTokenService
                        .findByToken(requestToken)
                        .orElseThrow(() -> new TokenRefreshException("Invalid refresh token"))
        );
        User user = tokenEntity.getUser();
        String newAccess = jwtTokenProvider.generateToken(user.getUsername());
        // rotate refresh token
        svc.revokeRefreshToken(requestToken);
        String newRefresh = svc.createRefreshToken(user.getId()).getToken();

        return new AuthResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                newAccess,
                newRefresh);
    }
}
