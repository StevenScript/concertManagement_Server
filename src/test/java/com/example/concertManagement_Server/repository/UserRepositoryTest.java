package com.example.concertManagement_Server.repository;

import com.example.concertManagement_Server.model.User;
import com.example.concertManagement_Server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save and retrieve user by username")
    void saveAndFindByUsername() {
        User u = User.builder()
                .username("john")
                .password("secret")
                .role("user")
                .email("john@example.com")
                .build();
        userRepository.save(u);

        Optional<User> found = userRepository.findByUsername("john");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("existsByUsername returns true only when user exists")
    void existsByUsername() {
        User u = User.builder()
                .username("alice")
                .password("pwd")
                .role("admin")
                .email("alice@example.com")
                .build();
        userRepository.save(u);

        assertThat(userRepository.existsByUsername("alice")).isTrue();
        assertThat(userRepository.existsByUsername("bob")).isFalse();
    }
}