/**
 * UserRepository.java
 *
 * Repository interface for User entities, providing standard CRUD operations
 * as well as custom lookups by username.
 *
 * Works closely with:
 * - User.java (entity model)
 * - AuthService.java (login and registration logic)
 * - UserDetailsServiceImpl.java (Spring Security user resolution)
 */
package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check
     * @return true if a user with that username exists, false otherwise
     */
    boolean existsByUsername(String username);
}
