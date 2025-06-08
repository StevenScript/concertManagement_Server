/**
 * UserDto.java
 *
 * A DTO representing user account information in API responses.
 * Used to expose user metadata without leaking sensitive fields like passwords.
 *
 * Used by:
 * - UserController.java (GET endpoints)
 * - Admin-level user management views
 */
package com.example.concertManagement_Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Unique ID of the user.
     */
    private Long id;

    /**
     * Username associated with the account.
     */
    private String username;

    /**
     * Email address used for login and contact.
     */
    private String email;

    /**
     * The user's assigned role (e.g., USER, ADMIN).
     */
    private String role;
}
