package com.example.concertManagement_Server.service;

import com.example.concertManagement_Server.exception.InvalidCredentialsException;
import com.example.concertManagement_Server.exception.UsernameAlreadyExistsException;
import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("register: success when username not already taken")
    void registerSuccess() {
        // given
        String username = "newuser";
        String rawPassword = "pass123";
        String email = "new@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        // simulate saving: assign an ID back
        ArgumentCaptor<User> saveCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(saveCaptor.capture()))
                .thenAnswer(invocation -> {
                    User u = saveCaptor.getValue();
                    u.setId(42L);
                    return u;
                });

        // when
        User created = authService.register(username, rawPassword, "user", email);

        // then
        assertThat(created.getId()).isEqualTo(42L);
        assertThat(created.getUsername()).isEqualTo(username);
        assertThat(created.getPassword()).isEqualTo(rawPassword);
        assertThat(created.getRole()).isEqualTo("user");
        assertThat(created.getEmail()).isEqualTo(email);

        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register: throws if username already exists")
    void registerFailsWhenUsernameTaken() {
        String username = "taken";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThatThrownBy(() ->
                authService.register(username, "whatever", "user", "e@e.com")
        ).isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining(username);

        verify(userRepository).existsByUsername(username);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("login: success when correct credentials")
    void loginSuccess() {
        String username = "bob";
        String password = "secret";
        User u = User.builder()
                .id(7L)
                .username(username)
                .password(password)
                .role("user")
                .email("bob@ex.com")
                .build();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(u));

        User loggedIn = authService.login(username, password);

        assertThat(loggedIn).isSameAs(u);
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("login: throws on unknown username")
    void loginFailsWhenNoUser() {
        when(userRepository.findByUsername("nobody"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                authService.login("nobody", "pw")
        ).isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByUsername("nobody");
    }

    @Test
    @DisplayName("login: throws on bad password")
    void loginFailsOnBadPassword() {
        String username = "alice";
        User u = User.builder()
                .username(username)
                .password("rightpw")
                .role("user")
                .email("a@e.com")
                .build();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(u));

        assertThatThrownBy(() ->
                authService.login(username, "wrongpw")
        ).isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByUsername(username);
    }
}