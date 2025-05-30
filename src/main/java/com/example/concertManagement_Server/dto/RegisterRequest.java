package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for user registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor  // (username, email, password, role)
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    /**
     * Convenience constructor defaulting role to "USER".
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
        this.role     = "USER";
    }
}
