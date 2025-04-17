package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor   // existing 4‑arg ctor: (username, email, password, role)
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    /**
     * Three‑arg ctor matching test: (username, email, password)
     * defaults role to "user"
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
        this.role     = "USER";
    }
}