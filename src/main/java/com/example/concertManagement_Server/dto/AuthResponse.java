/**
 * AuthResponse.java
 *
 * DTO used to return authenticated session data to the client after login, registration,
 * or token refresh. This includes identity, role, and both access and refresh tokens.
 *
 * Used by:
 * - AuthController.java (register, login, refresh)
 * - Frontend session/token handling
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    /**
     * Unique ID of the authenticated user.
     */
    private Long id;

    /**
     * The user's display name or login username.
     */
    private String username;

    /**
     * The user's registered email address.
     */
    private String email;

    /**
     * The user's assigned role (e.g., USER, ADMIN).
     */
    private String role;

    /**
     * Short-lived access token (typically JWT) for authorizing requests.
     */
    private String accessToken;

    /**
     * Long-lived refresh token used to request new access tokens.
     */
    private String refreshToken;
}
