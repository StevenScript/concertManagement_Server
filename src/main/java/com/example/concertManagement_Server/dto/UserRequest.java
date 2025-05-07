package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Payload for creating/updating a user. */
@Data @NoArgsConstructor @AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String role;     // "USER" or "ADMIN"
    private String password; // nullable on update
}