package com.jobber.auth.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing user authentication and profile information.
 * This class stores user credentials, profile details, and
 * authentication-related data.
 */
@Entity
@Table(name = "auths")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  @Column(unique = true, nullable = false)
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one digit, one uppercase, one lowercase letter, and one special character")
  @Column(nullable = false)
  private String password;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "profile_public_id")
  private String profilePublicId;

  @Size(max = 100, message = "Country name must not exceed 100 characters")
  private String country;

  @Column(name = "profile_picture")
  private String profilePicture;

  @Column(name = "email_verification_token")
  private String emailVerificationToken;

  @Column(name = "email_verified", nullable = false)
  private boolean emailVerified = false;

  @Column(name = "browser_name")
  private String browserName;

  @Column(name = "device_type")
  private String deviceType;

  @Column(name = "otp")
  private String otp;

  @Column(name = "otp_expiration")
  private LocalDateTime otpExpiration;

  @Column(name = "password_reset_token")
  private String passwordResetToken;

  @Column(name = "password_reset_token_expiration")
  private LocalDateTime passwordResetTokenExpiration;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Automatically sets creation and update timestamps before persisting the
   * entity.
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * Automatically updates the timestamp whenever the entity is modified.
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  /**
   * Checks if the OTP is valid and not expired.
   * 
   * @return true if OTP exists and is not expired, false otherwise
   */
  public boolean isOtpValid() {
    return otp != null && otpExpiration != null &&
        LocalDateTime.now().isBefore(otpExpiration);
  }

  /**
   * Checks if the password reset token is valid and not expired.
   * 
   * @return true if token exists and is not expired, false otherwise
   */
  public boolean isPasswordResetTokenValid() {
    return passwordResetToken != null &&
        passwordResetTokenExpiration != null &&
        LocalDateTime.now().isBefore(passwordResetTokenExpiration);
  }

  /**
   * Updates the last login timestamp to the current time.
   */
  public void updateLastLogin() {
    this.lastLogin = LocalDateTime.now();
  }

  /**
   * Generates a new OTP and sets its expiration time.
   * 
   * @param otpLength         Length of the OTP to generate
   * @param expirationMinutes Minutes until OTP expires
   */
  public void generateOtp(int otpLength, int expirationMinutes) {
    this.otp = String.format("%0" + otpLength + "d",
        (int) (Math.random() * Math.pow(10, otpLength)));
    this.otpExpiration = LocalDateTime.now().plusMinutes(expirationMinutes);
  }

  /**
   * Generates a new password reset token and sets its expiration time.
   * 
   * @param expirationHours Hours until token expires
   */
  public void generatePasswordResetToken(int expirationHours) {
    this.passwordResetToken = UUID.randomUUID().toString();
    this.passwordResetTokenExpiration = LocalDateTime.now().plusHours(expirationHours);
  }

  /**
   * Clears sensitive authentication data.
   * Should be called after successful authentication or when resetting
   * credentials.
   */
  public void clearSensitiveData() {
    this.otp = null;
    this.otpExpiration = null;
    this.passwordResetToken = null;
    this.passwordResetTokenExpiration = null;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }
}
