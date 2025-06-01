package com.jobber.auth.controller;


import com.jobber.auth.controllers.AuthController;
import com.jobber.auth.domain.dtos.request.LoginRequest;
import com.jobber.auth.domain.dtos.request.SignUpRequest;
import com.jobber.auth.domain.dtos.response.AuthResponse;
import com.jobber.auth.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private BCryptPasswordEncoder passwordEncoder;

    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .username("existing")
                .email("existing@example.com")
                .password(passwordEncoder.encode("Password123"))
                .browserName("Chrome")
                .deviceType("Desktop")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        authResponse = AuthResponse.builder()
                .token("token123")
                .userId(UUID.randomUUID())
                .build();
    }

    @Test
    void signUp_ValidRequest_ReturnsOkResponse(){
        // Arrange
        when(authService.register(signUpRequest)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.signUp(signUpRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authService, times(1)).register(signUpRequest);
    }

    @Test
    void login_ValidCredentials_ReturnsOkResponse() {
        // Arrange
        when(authService.login(loginRequest)).thenReturn(authResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void signUp_NullRequest_ThrowsException() {
        // Arrange
        when(authService.register(null)).thenThrow(new IllegalArgumentException("Request cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.signUp(null));
        verify(authService, times(1)).register(null);
    }

    @Test
    void login_NullRequest_ThrowsException() {
        // Arrange
        when(authService.login(null)).thenThrow(new IllegalArgumentException("Request cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.login(null));
        verify(authService, times(1)).login(null);
    }

    @Test
    void signUp_ServiceThrowsException_PropagatesException() {
        // Arrange
        when(authService.register(signUpRequest)).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.signUp(signUpRequest));
        verify(authService, times(1)).register(signUpRequest);
    }

    @Test
    void login_ServiceThrowsException_PropagatesException() {
        // Arrange
        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
        verify(authService, times(1)).login(loginRequest);
    }
}
