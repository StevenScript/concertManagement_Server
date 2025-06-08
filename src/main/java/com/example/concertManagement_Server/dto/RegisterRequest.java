/**
 * RegisterRequest.java
 *
 * Payload submitted during user registration.
 * Includes fields for initial account setup, including optional role assignment.
 *
 * Used by:
 * - AuthController.java (POST /register)
 * - AuthService.java (user creation logic)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor  // Full constructor for custom role setup
public class RegisterRequest {

    /**
     * Desired username for login/display.
     */
    private String username;

    /**
     * Email address to associate with the account.
     */
    private String email;

    /**
     * Plain-text password to be hashed before storage.
     */
    private String password;

    /**
     * Assigned user role (defaults to "USER" if omitted).
     */
    private String role;

    /**
     * Convenience constructor for basic registration (defaults role to "USER").
     *
     * @param username the desired username
     * @param email the user's email
     * @param password the user's password
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
        this.role     = "USER";
    }
}
