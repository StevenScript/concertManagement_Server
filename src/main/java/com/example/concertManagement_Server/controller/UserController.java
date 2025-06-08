/**
 * UserController.java
 *
 * REST controller for managing application users.
 * Provides full CRUD operations, generally restricted to admin-level usage.
 *
 * Works closely with:
 * - UserService.java (user business logic)
 * - UserDto / UserRequest (DTOs for transport and creation)
 */
package com.example.concertManagement_Server.controller;

import com.example.concertManagement_Server.dto.UserDto;
import com.example.concertManagement_Server.dto.UserRequest;
import com.example.concertManagement_Server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of all UserDto records
     */
    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the user's ID
     * @return 200 OK with user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Creates a new user in the system.
     *
     * @param req the user creation request payload
     * @return 201 Created with user details
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    /**
     * Updates a user by ID.
     *
     * @param id  the user ID
     * @param req updated user fields
     * @return updated user as UserDto
     */
    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserRequest req) {
        return service.update(id, req);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

