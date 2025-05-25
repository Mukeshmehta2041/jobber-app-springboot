package com.jobber.auth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response.
 * Contains user information and authentication token after successful
 * login/registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private UUID id;
  private String username;
  private String email;
  private String country;
  private String profilePicture;
  private Boolean emailVerified;
  private String accessToken;
  private String refreshToken;
  private LocalDateTime lastLogin;
  private LocalDateTime createdAt;
}