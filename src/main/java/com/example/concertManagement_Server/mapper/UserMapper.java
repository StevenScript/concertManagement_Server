package com.example.concertManagement_Server.mapper;

import com.example.concertManagement_Server.dto.UserDto;
import com.example.concertManagement_Server.dto.UserRequest;
import com.example.concertManagement_Server.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /** Entity → DTO */
    public UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
    }

    /** DTO → new Entity */
    public User toEntity(UserRequest req, String encodedPwd) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setRole(req.getRole());
        u.setPassword(encodedPwd);
        return u;
    }

    /** Update existing entity from request */
    public void updateEntity(UserRequest req, User u, String encodedPwd) {
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setRole(req.getRole());
        if (encodedPwd != null) u.setPassword(encodedPwd);
    }
}