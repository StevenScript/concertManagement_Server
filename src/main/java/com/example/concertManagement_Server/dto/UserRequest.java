/**
 * UserRequest.java
 *
 * Payload for creating or updating user accounts.
 * Used by both admin-level user management and initial registration flow.
 *
 * Used by:
 * - UserController.java (POST and PUT endpoints)
 * - AuthService.java (registration)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    /**
     * Desired username (used for login and display).
     */
    private String username;

    /**
     * Email address associated with the account.
     */
    private String email;

    /**
     * User role ("USER" or "ADMIN").
     */
    private String role;

    /**
     * Password for account access. May be null during updates.
     */
    private String password;
}
