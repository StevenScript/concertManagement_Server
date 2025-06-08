/**
 * LoginAttemptService.java
 *
 * Service that tracks failed login attempts per user and enforces temporary lockout
 * after repeated failures. Useful for basic brute-force protection.
 *
 * Works closely with:
 * - AuthService.java (failed login tracking and lock enforcement)
 */
package com.example.concertManagement_Server.security;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 3;
    private final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockTime = new ConcurrentHashMap<>();

    /**
     * Clears the failure history for a user after successful login.
     *
     * @param username the username to clear
     */
    public void loginSucceeded(String username) {
        attempts.remove(username);
        lockTime.remove(username);
    }

    /**
     * Records a failed login attempt and locks the account if limits exceeded.
     *
     * @param username the username to track
     */
    public void loginFailed(String username) {
        if (isLocked(username)) return;

        int count = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, count);
        if (count >= MAX_ATTEMPTS) {
            lockTime.put(username, LocalDateTime.now());
        }
    }

    /**
     * Checks if the user is currently locked out.
     *
     * @param username the username to check
     * @return true if locked, false otherwise
     */
    public boolean isLocked(String username) {
        LocalDateTime lockedAt = lockTime.get(username);
        if (lockedAt == null) return false;

        if (Duration.between(lockedAt, LocalDateTime.now()).compareTo(LOCK_DURATION) >= 0) {
            attempts.remove(username);
            lockTime.remove(username);
            return false;
        }

        return true;
    }

    /**
     * Returns remaining lockout time in seconds.
     *
     * @param username the username to check
     * @return seconds remaining in lockout
     */
    public long getRemainingLockSeconds(String username) {
        LocalDateTime lockedAt = lockTime.get(username);
        if (lockedAt == null) return 0;

        long passed = Duration.between(lockedAt, LocalDateTime.now()).getSeconds();
        return Math.max(0, LOCK_DURATION.getSeconds() - passed);
    }
}
