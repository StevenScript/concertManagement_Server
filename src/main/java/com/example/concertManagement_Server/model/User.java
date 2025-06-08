package com.example.concertManagement_Server.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user of the concert system (admin or regular).
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username used to log in. */
    @Column(unique = true, nullable = false)
    private String username;

    /** Hashed user password. */
    @Column(nullable = false)
    private String password;

    /** Role of the user (e.g., ROLE_USER, ROLE_ADMIN). */
    @Column(nullable = false)
    private String role;

    /** Optional email, must be unique if provided. */
    @Column(unique = true)
    private String email;
}
