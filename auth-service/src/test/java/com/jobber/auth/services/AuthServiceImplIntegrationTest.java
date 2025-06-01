package com.jobber.auth.services;

import com.jobber.auth.domain.dtos.request.LoginRequest;
import com.jobber.auth.domain.dtos.request.SignUpRequest;
import com.jobber.auth.domain.dtos.response.AuthResponse;
import com.jobber.auth.entities.Auth;
import com.jobber.auth.exceptions.ResourceAlreadyExistException;
import com.jobber.auth.exceptions.ResourceNotFoundException;
import com.jobber.auth.repositories.AuthRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for {@link AuthServiceImpl}.
 * This test class verifies registration and login logic using an H2 embedded database.
 */
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@Import(BCryptPasswordEncoder.class)
public class AuthServiceImplIntegrationTest {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Sets up a known user in the repository before each test.
     */
    @BeforeEach
    void setUp() {
        authRepository.deleteAll();

        Auth existingUser = Auth.builder()
                .username("existing")
                .email("existing@example.com")
                .password(passwordEncoder.encode("Password123"))
                .browserName("Chrome")
                .deviceType("Desktop")
                .build();

        authRepository.save(existingUser);
    }

    /**
     * Test: Should register a new user successfully.
     */
    @Test
    void shouldRegisterNewUser() {
        SignUpRequest request = SignUpRequest.builder()
                .username("mukesh")
                .email("mukesh@example.com")
                .password("Password@123")
                .browserName("Firefox")
                .deviceType("Laptop")
                .country("India")
                .build();

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotEmpty();
        assertThat(authRepository.findByEmailOrUsername("mukesh@example.com", "mukesh")).isPresent();
    }

    /**
     * Test: Should throw an exception when trying to register a user that already exists.
     */
    @Test
    void shouldThrowWhenUserExists() {
        SignUpRequest duplicate = SignUpRequest.builder()
                .username("existing")
                .email("existing@example.com")
                .password("SomePass")
                .build();

        assertThatThrownBy(() -> authService.register(duplicate))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessageContaining("already exists");
    }

    /**
     * Test: Should login successfully with correct credentials.
     */
    @Test
    void shouldLoginSuccessfully() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("existing")
                .password("Password123")
                .browserName("Chrome")
                .build();

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotEmpty();
    }

    /**
     * Test: Should fail login due to wrong password.
     */
    @Test
    void shouldFailLoginForWrongPassword() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("existing")
                .password("wrong")
                .build();

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invalid credentials");
    }

    /**
     * Test: Should fail login due to unknown user.
     */
    @Test
    void shouldFailLoginForUnknownUser() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("unknown")
                .password("Password")
                .build();

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invalid credentials");
    }
}
