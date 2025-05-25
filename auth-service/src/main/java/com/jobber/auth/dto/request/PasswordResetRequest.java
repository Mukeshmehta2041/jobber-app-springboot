package com.jobber.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password reset request.
 * Contains the new password and reset token for password reset operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

  @NotBlank(message = "Reset token is required")
  private String token;

  @NotBlank(message = "New password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one digit, one uppercase, one lowercase letter, and one special character")
  private String newPassword;
}