package com.example.concertManagement_Server.security;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks failed login attempts and locks out after 3 strikes for 15 minutes.
 */
@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 3;
    private final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockTime = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
        lockTime.remove(username);
    }

    public void loginFailed(String username) {
        if (isLocked(username)) {
            return;
        }
        int count = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, count);
        if (count >= MAX_ATTEMPTS) {
            lockTime.put(username, LocalDateTime.now());
        }
    }

    public boolean isLocked(String username) {
        LocalDateTime lockedAt = lockTime.get(username);
        if (lockedAt == null) return false;
        if (Duration.between(lockedAt, LocalDateTime.now()).compareTo(LOCK_DURATION) >= 0) {
            // expired
            attempts.remove(username);
            lockTime.remove(username);
            return false;
        }
        return true;
    }

    public long getRemainingLockSeconds(String username) {
        LocalDateTime lockedAt = lockTime.get(username);
        if (lockedAt == null) return 0;
        long passed = Duration.between(lockedAt, LocalDateTime.now()).getSeconds();
        return Math.max(0, LOCK_DURATION.getSeconds() - passed);
    }
}