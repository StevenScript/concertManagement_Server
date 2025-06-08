/**
 * JwtTokenProvider.java
 *
 * Utility class for generating, parsing, and validating JWT tokens using HS256.
 * Reads secret key and expiration window from application properties.
 *
 * Works closely with:
 * - JwtAuthFilter.java (authentication filter)
 * - AuthService.java (token issuing)
 */
package com.example.concertManagement_Server.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityMillis;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-ms}") long validityMillis) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityMillis = validityMillis;
    }

    /**
     * Generates a signed JWT containing the username and expiration time.
     *
     * @param username the subject of the token
     * @return signed JWT as a String
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from the provided JWT.
     *
     * @param token the JWT string
     * @return subject (username) of the token
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Verifies the integrity and expiration of a JWT.
     *
     * @param token the JWT to validate
     * @return true if valid; false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
