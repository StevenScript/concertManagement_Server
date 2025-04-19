package com.example.concertManagement_Server.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a system user with credentials and role.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username for authentication, must be unique.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Encrypted password for the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role assigned to the user (e.g., ROLE_USER, ROLE_ADMIN).
     */
    @Column(nullable = false)
    private String role;

    /**
     * Email address for the user, may be null.
     */
    @Column(unique = true)
    private String email;
}