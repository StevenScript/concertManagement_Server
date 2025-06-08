/**
 * RefreshTokenRequest.java
 *
 * Payload for requesting a new access token using an existing valid refresh token.
 * This object is submitted to the `/api/refresh` endpoint.
 *
 * Used by:
 * - AuthController.java (POST /refresh)
 * - AuthService.java (refresh logic)
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    /**
     * Long-lived refresh token used to request a new access token.
     */
    private String refreshToken;
}

