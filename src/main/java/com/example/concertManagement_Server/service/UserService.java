package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.UserDto;
import com.example.concertManagement_Server.dto.UserRequest;
import com.example.concertManagement_Server.exception.ResourceNotFoundException;
import com.example.concertManagement_Server.mapper.UserMapper;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository  repo;
    private final UserMapper      mapper;
    private final PasswordEncoder encoder; // inject the bean you already use

    public UserService(UserRepository repo, UserMapper mapper,
                       PasswordEncoder encoder) {
        this.repo = repo;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    /* ---------- list ---------- */
    public List<UserDto> getAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    /* ---------- single ---------- */
    public UserDto getById(Long id) {
        return mapper.toDto(
                repo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("User id " + id + " not found")));
    }

    /* ---------- create ---------- */
    public UserDto create(UserRequest req) {
        String hashed = encoder.encode(req.getPassword());
        User saved = repo.save(mapper.toEntity(req, hashed));
        return mapper.toDto(saved);
    }

    /* ---------- update ---------- */
    public UserDto update(Long id, UserRequest req) {
        User u = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User id " + id + " not found"));
        String hashed = req.getPassword() != null && !req.getPassword().isBlank()
                ? encoder.encode(req.getPassword())
                : null;
        mapper.updateEntity(req, u, hashed);
        return mapper.toDto(repo.save(u));
    }

    /* ---------- delete ---------- */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
