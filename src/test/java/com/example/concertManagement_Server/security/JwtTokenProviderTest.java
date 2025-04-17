package com.example.concertManagement_Server.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;

    @BeforeEach
    void setup() {
        String secret   = "VerySecretKeyForJwtSigningThatIsAtLeast256BitsLong123!";
        long validityMillis = 10_000;                 // 1 s – keeps the test fast
        provider        = new JwtTokenProvider(secret, validityMillis);
    }

    @Test
    void generateAndValidateToken() {
        String token = provider.generateToken("alice");
        assertTrue(provider.validateToken(token), "token should validate");
        assertEquals("alice", provider.getUsername(token));
    }

    @Test
    void invalidTokenFailsValidation() {
        assertFalse(provider.validateToken("garbage"));
    }
}