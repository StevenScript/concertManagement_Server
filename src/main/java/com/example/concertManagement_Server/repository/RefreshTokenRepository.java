/**
 * RefreshTokenRepository.java
 *
 * Repository for managing RefreshToken entities used in JWT authentication.
 * Supports operations such as:
 * - Looking up a token string
 * - Deleting tokens associated with a specific user
 *
 * Works closely with:
 * - RefreshToken.java (entity)
 * - RefreshTokenService.java (token lifecycle logic)
 * - AuthService.java (authentication integration)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a refresh token by its token string.
     *
     * @param token the raw token string
     * @return an Optional containing the token if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes all refresh tokens associated with a specific user ID.
     *
     * @param userId the user's ID
     * @return number of deleted records
     */
    int deleteByUserId(Long userId);
}
