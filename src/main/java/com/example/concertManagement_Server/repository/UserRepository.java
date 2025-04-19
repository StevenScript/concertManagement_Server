package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User entities, providing CRUD operations
 * and lookups by username.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user with the given username already exists.
     *
     * @param username the username to check for
     * @return true if a user exists with that username, false otherwise
     */
    boolean existsByUsername(String username);
}