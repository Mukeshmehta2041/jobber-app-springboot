package com.jobber.auth.repositories;

import com.jobber.auth.entities.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AuthRepository}.
 * Uses H2 in-memory database for isolated testing.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        authRepository.save(Auth.builder()
                .username("mukesh123")
                .password("Password@123")
                .email("mukesh@example.com")
                .emailVerified(true)
                .build());

        authRepository.save(Auth.builder()
                .username("john_doe")
                .password("Secure@123")
                .email("john@example.com")
                .emailVerified(false)
                .build());
    }

    /**
     * Test to verify that user can be found by email.
     */
    @Test
    void shouldFindByEmail() {
        Optional<Auth> result = authRepository.findByEmailOrUsername("mukesh@example.com", "unknown");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("mukesh123");
    }

    /**
     * Test to verify that user can be found by username.
     */
    @Test
    void shouldFindByUsername() {
        Optional<Auth> result = authRepository.findByEmailOrUsername("unknown@example.com", "john_doe");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    /**
     * Test to verify that no result is returned when both email and username are not found.
     */
    @Test
    void shouldReturnEmptyWhenNoMatch() {
        Optional<Auth> result = authRepository.findByEmailOrUsername("notfound@example.com", "notfound");
        assertThat(result).isNotPresent();
    }
}
