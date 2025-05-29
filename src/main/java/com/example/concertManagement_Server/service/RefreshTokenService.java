package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.TokenRefreshException;
import com.example.concertManagement_Server.model.RefreshToken;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.RefreshTokenRepository;
import com.example.concertManagement_Server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${security.jwt.refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new refresh token for the given user, revoking any prior one.
     */
    public RefreshToken createRefreshToken(Long userId) {
        // revoke any existing
        refreshTokenRepository.deleteByUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TokenRefreshException("User not found: " + userId));

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        token.setRevoked(false);

        return refreshTokenRepository.save(token);
    }

    /**
     * Look up a stored RefreshToken by its token string.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verify that a refresh token has not expired or been revoked.
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new TokenRefreshException("Refresh token expired. Please login again.");
        }
        if (token.isRevoked()) {
            throw new TokenRefreshException("Refresh token revoked. Please login again.");
        }
        return token;
    }

    /**
     * Revoke (blacklist) a given refresh token so it cannot be reused.
     */
    @Transactional
    public void revokeRefreshToken(String tokenStr) {
        refreshTokenRepository.findByToken(tokenStr).ifPresent(t -> {
            t.setRevoked(true);
            refreshTokenRepository.save(t);
        });
    }
}

