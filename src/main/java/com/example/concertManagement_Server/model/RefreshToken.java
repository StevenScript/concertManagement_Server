package com.example.concertManagement_Server.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents a refresh token issued to a user for JWT-based authentication.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The actual refresh token string (must be unique). */
    @Column(nullable = false, unique = true)
    private String token;

    /** When this token will expire. */
    @Column(nullable = false)
    private Instant expiryDate;

    /** The user this token was issued to. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** If true, this token has been manually revoked. */
    @Column(nullable = false)
    private boolean revoked = false;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
