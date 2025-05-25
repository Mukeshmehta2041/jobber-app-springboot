package com.jobber.auth.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobber.auth.dto.request.LoginRequest;
import com.jobber.auth.dto.request.PasswordResetRequest;
import com.jobber.auth.dto.request.RegisterRequest;
import com.jobber.auth.dto.response.AuthResponse;
import com.jobber.auth.dto.response.ProfileResponse;
import com.jobber.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for handling authentication and user management operations.
 * Provides endpoints for user registration, login, profile management, and
 * account operations.
 * 
 * All endpoints are prefixed with /api/v1/auth
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * Registers a new user in the system.
   * 
   * @param request Registration request containing user details
   * @return AuthResponse containing user information and tokens
   * @throws ValidationException if email or username is already taken
   */
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    log.info("Received registration request for email: {}", request.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(authService.register(request));
  }

  /**
   * Authenticates a user and generates access tokens.
   * 
   * @param request Login request containing credentials
   * @return AuthResponse containing user information and tokens
   * @throws AuthenticationException if credentials are invalid or email is not
   *                                 verified
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("Received login request for email: {}", request.getEmail());
    return ResponseEntity.ok(authService.login(request));
  }

  /**
   * Retrieves user profile information.
   * 
   * @param userId ID of the user
   * @return ProfileResponse containing user profile details
   * @throws UserNotFoundException if user is not found
   */
  @GetMapping("/profile/{userId}")
  public ResponseEntity<ProfileResponse> getProfile(@PathVariable UUID userId) {
    log.info("Received profile request for user: {}", userId);
    return ResponseEntity.ok(authService.getProfile(userId));
  }

  /**
   * Updates user profile information.
   * 
   * @param userId  ID of the user
   * @param request Registration request containing updated information
   * @return ProfileResponse containing updated user profile
   * @throws UserNotFoundException if user is not found
   */
  @PutMapping("/profile/{userId}")
  public ResponseEntity<ProfileResponse> updateProfile(
      @PathVariable UUID userId,
      @Valid @RequestBody RegisterRequest request) {
    log.info("Received profile update request for user: {}", userId);
    return ResponseEntity.ok(authService.updateProfile(userId, request));
  }

  /**
   * Initiates the password reset process.
   * 
   * @param email Email of the user
   * @return Empty response with 200 status
   * @throws UserNotFoundException if user is not found
   */
  @PostMapping("/password/reset/initiate")
  public ResponseEntity<Void> initiatePasswordReset(@RequestParam String email) {
    log.info("Received password reset initiation request for email: {}", email);
    authService.initiatePasswordReset(email);
    return ResponseEntity.ok().build();
  }

  /**
   * Resets the user's password using a reset token.
   * 
   * @param request Password reset request containing token and new password
   * @return Empty response with 200 status
   * @throws TokenException if token is invalid or expired
   */
  @PostMapping("/password/reset")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
    log.info("Received password reset request");
    authService.resetPassword(request);
    return ResponseEntity.ok().build();
  }

  /**
   * Verifies user's email using verification token.
   * 
   * @param token Email verification token
   * @return Empty response with 200 status
   * @throws TokenException if token is invalid
   */
  @PostMapping("/email/verify")
  public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
    log.info("Received email verification request");
    authService.verifyEmail(token);
    return ResponseEntity.ok().build();
  }

  /**
   * Resends email verification token.
   * 
   * @param email Email of the user
   * @return Empty response with 200 status
   * @throws UserNotFoundException if user is not found
   * @throws ValidationException   if email is already verified
   */
  @PostMapping("/email/verify/resend")
  public ResponseEntity<Void> resendVerificationEmail(@RequestParam String email) {
    log.info("Received email verification resend request for email: {}", email);
    authService.resendVerificationEmail(email);
    return ResponseEntity.ok().build();
  }

  /**
   * Deletes a user account.
   * 
   * @param userId ID of the user
   * @return Empty response with 200 status
   * @throws UserNotFoundException if user is not found
   */
  @DeleteMapping("/account/{userId}")
  public ResponseEntity<Void> deleteAccount(@PathVariable UUID userId) {
    log.info("Received account deletion request for user: {}", userId);
    authService.deleteAccount(userId);
    return ResponseEntity.ok().build();
  }

  /**
   * Checks if a username is available.
   * 
   * @param username Username to check
   * @return true if username is available, false otherwise
   */
  @GetMapping("/check/username")
  public ResponseEntity<Boolean> isUsernameAvailable(@RequestParam String username) {
    log.info("Received username availability check request for username: {}", username);
    return ResponseEntity.ok(authService.isUsernameAvailable(username));
  }

  /**
   * Checks if an email is available.
   * 
   * @param email Email to check
   * @return true if email is available, false otherwise
   */
  @GetMapping("/check/email")
  public ResponseEntity<Boolean> isEmailAvailable(@RequestParam String email) {
    log.info("Received email availability check request for email: {}", email);
    return ResponseEntity.ok(authService.isEmailAvailable(email));
  }
}