package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.exception.AccountLockedException;
import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.TokenRefreshException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.RefreshToken;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import com.example.concertManagement_Server.security.JwtTokenProvider;
import com.example.concertManagement_Server.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final JwtTokenProvider    jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    /**
     * Register a new account, immediately issue both tokens.
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        //if (userRepository.existsByEmail(req.getEmail())) {
        //    throw new UsernameAlreadyExistsException("Email already registered");
        // }

        // Build and save new user
        User userToSave = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        // Save and get the instance WITH ID populated!
        User saved = userRepository.save(userToSave);

        String accessToken  = jwtTokenProvider.generateToken(saved.getUsername());
        String refreshToken = refreshTokenService
                .createRefreshToken(saved.getId())
                .getToken();

        return new AuthResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole(),
                accessToken,
                refreshToken
        );
    }

    /**
     * Authenticate credentials, enforce lockout policy, issue both tokens.
     */
    public AuthResponse login(LoginRequest req) {
        String username = req.getUsername();

        if (loginAttemptService.isLocked(username)) {
            long secs = loginAttemptService.getRemainingLockSeconds(username);
            throw new AccountLockedException(
                    "Account locked. Try again in " + secs + " seconds."
            );
        }

        User u = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid username or password")
                );

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            loginAttemptService.loginFailed(username);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        loginAttemptService.loginSucceeded(username);

        String accessToken  = jwtTokenProvider.generateToken(u.getUsername());
        String refreshToken = refreshTokenService
                .createRefreshToken(u.getId())
                .getToken();

        return new AuthResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                accessToken,
                refreshToken
        );
    }

    /**
     * Rotate refresh token: validate, revoke the old, issue a fresh pair.
     */
    public AuthResponse refreshToken(String requestRefreshToken) {
        RefreshToken stored = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() ->
                        new TokenRefreshException("Refresh token not found: " + requestRefreshToken)
                );
        refreshTokenService.verifyExpiration(stored);

        // revoke old and issue new
        refreshTokenService.revokeRefreshToken(requestRefreshToken);
        User u = stored.getUser();

        String newRefreshToken = refreshTokenService
                .createRefreshToken(u.getId())
                .getToken();
        String newAccessToken  = jwtTokenProvider.generateToken(u.getUsername());

        return new AuthResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                newAccessToken,
                newRefreshToken
        );
    }
}
