package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service handling authentication: registration and login.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user if the username is not already taken.
     * @param username desired username
     * @param password raw password
     * @param role user role (e.g., "user", "admin")
     * @param email user email address
     * @return the created User entity
     * @throws UsernameAlreadyExistsException when username is already in use
     */
    public User register(String username, String password, String role, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already taken: " + username);
        }
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    /**
     * Authenticates a user by username and password.
     * @param username user's username
     * @param password user's password
     * @return the authenticated User entity
     * @throws InvalidCredentialsException when username not found or password mismatch
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user;
    }
}