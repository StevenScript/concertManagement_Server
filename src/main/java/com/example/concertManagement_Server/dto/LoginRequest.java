/**
 * LoginRequest.java
 *
 * Payload for user login requests.
 * Used to submit credentials for authentication.
 *
 * Used by:
 * - AuthController.java (POST /login)
 * - AuthService.java (login logic)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * The user's login name.
     */
    private String username;

    /**
     * The user's password.
     */
    private String password;
}
