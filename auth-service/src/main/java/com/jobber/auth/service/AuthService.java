package com.jobber.auth.service;

import java.util.UUID;

import com.jobber.auth.dto.request.LoginRequest;
import com.jobber.auth.dto.request.PasswordResetRequest;
import com.jobber.auth.dto.request.RegisterRequest;
import com.jobber.auth.dto.response.AuthResponse;
import com.jobber.auth.dto.response.ProfileResponse;

/**
 * Service interface for handling authentication and user management operations.
 */
public interface AuthService {

  /**
   * Registers a new user.
   * 
   * @param request Registration request containing user details
   * @return AuthResponse containing user information and tokens
   */
  AuthResponse register(RegisterRequest request);

  /**
   * Authenticates a user and generates tokens.
   * 
   * @param request Login request containing credentials
   * @return AuthResponse containing user information and tokens
   */
  AuthResponse login(LoginRequest request);

  /**
   * Retrieves user profile information.
   * 
   * @param userId ID of the user
   * @return ProfileResponse containing user profile details
   */
  ProfileResponse getProfile(UUID userId);

  /**
   * Updates user profile information.
   * 
   * @param userId  ID of the user
   * @param request Registration request containing updated information
   * @return ProfileResponse containing updated user profile
   */
  ProfileResponse updateProfile(UUID userId, RegisterRequest request);

  /**
   * Initiates the password reset process.
   * 
   * @param email Email of the user
   */
  void initiatePasswordReset(String email);

  /**
   * Resets the user's password using a reset token.
   * 
   * @param request Password reset request containing token and new password
   */
  void resetPassword(PasswordResetRequest request);

  /**
   * Verifies user's email using verification token.
   * 
   * @param token Email verification token
   */
  void verifyEmail(String token);

  /**
   * Resends email verification token.
   * 
   * @param email Email of the user
   */
  void resendVerificationEmail(String email);

  /**
   * Deletes a user account.
   * 
   * @param userId ID of the user
   */
  void deleteAccount(UUID userId);

  /**
   * Checks if a username is available.
   * 
   * @param username Username to check
   * @return true if username is available, false otherwise
   */
  boolean isUsernameAvailable(String username);

  /**
   * Checks if an email is available.
   * 
   * @param email Email to check
   * @return true if email is available, false otherwise
   */
  boolean isEmailAvailable(String email);
}