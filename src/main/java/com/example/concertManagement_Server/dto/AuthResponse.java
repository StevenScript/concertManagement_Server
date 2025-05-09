package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned after authentication or registration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long   id;
    private String username;
    private String email;
    private String role;
    private String token;
}