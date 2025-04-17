package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.dto.AuthResponse;
import com.example.concertManagement_Server.dto.LoginRequest;
import com.example.concertManagement_Server.dto.RegisterRequest;
import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository    = mock(UserRepository.class);
        passwordEncoder   = mock(PasswordEncoder.class);
        authService       = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void register_success() {
        // given
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("hashed");
        // when
        RegisterRequest req = new RegisterRequest("newuser", "new@ex.com", "pass123");
        AuthResponse resp  = authService.register(req);
        // then
        // saved user has correct fields
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getEmail()).isEqualTo("new@ex.com");
        assertThat(saved.getPassword()).isEqualTo("hashed");
        assertThat(saved.getRole()).isEqualTo("USER");

        // response contains a token and username/role
        assertThat(resp.getUsername()).isEqualTo("newuser");
        assertThat(resp.getRole()).isEqualTo("USER");
        assertThat(resp.getToken()).isNotBlank();
    }

    @Test
    void register_failsWhenUsernameExists() {
        when(userRepository.existsByUsername("taken")).thenReturn(true);
        RegisterRequest req = new RegisterRequest("taken", "a@b.com", "xyz");
        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username already taken");
    }

    @Test
    void login_success() {
        // given
        User u = User.builder()
                .username("u1")
                .password("hashedpw")
                .role("USER")
                .build();
        when(userRepository.findByUsername("u1")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("pw", "hashedpw")).thenReturn(true);

        // when
        LoginRequest req = new LoginRequest("u1", "pw");
        AuthResponse resp = authService.login(req);

        // then
        assertThat(resp.getUsername()).isEqualTo("u1");
        assertThat(resp.getRole()).isEqualTo("USER");
        assertThat(resp.getToken()).isNotBlank();
    }

    @Test
    void login_failsWhenUserNotFound() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());
        LoginRequest req = new LoginRequest("nope", "anything");
        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid username or password");
    }

    @Test
    void login_failsWhenPasswordMismatch() {
        User u = User.builder()
                .username("u2")
                .password("hash2")
                .role("USER")
                .build();
        when(userRepository.findByUsername("u2")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("wrong", "hash2")).thenReturn(false);

        LoginRequest req = new LoginRequest("u2", "wrong");
        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Invalid username or password");
    }
}