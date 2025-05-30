package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for requesting a new access token via a refresh token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}
