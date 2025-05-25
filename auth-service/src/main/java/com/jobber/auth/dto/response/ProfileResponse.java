package com.jobber.auth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user profile response.
 * Contains detailed user profile information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
  private UUID id;
  private String username;
  private String email;
  private String country;
  private String profilePicture;
  private Boolean emailVerified;
  private String browserName;
  private String deviceType;
  private LocalDateTime lastLogin;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}