package com.jobber.auth.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobber.auth.dto.request.LoginRequest;
import com.jobber.auth.dto.request.PasswordResetRequest;
import com.jobber.auth.dto.request.RegisterRequest;
import com.jobber.auth.dto.response.AuthResponse;
import com.jobber.auth.dto.response.ProfileResponse;
import com.jobber.auth.entities.Auth;
import com.jobber.auth.exception.AuthenticationException;
import com.jobber.auth.exception.TokenException;
import com.jobber.auth.exception.UserNotFoundException;
import com.jobber.auth.exception.ValidationException;
import com.jobber.auth.mapper.AuthMapper;
import com.jobber.auth.repository.AuthRepository;
import com.jobber.auth.service.AuthService;
import com.jobber.auth.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the AuthService interface.
 * Handles user authentication, registration, and profile management operations.
 * 
 * This service is responsible for:
 * - User registration and email verification
 * - User authentication and token management
 * - Profile management
 * - Password reset functionality
 * - Account deletion
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthRepository authRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final AuthMapper authMapper;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  private static final int PASSWORD_RESET_TOKEN_EXPIRY_HOURS = 24;
  private static final String EMAIL_TOPIC = "email-topic";

  /**
   * Registers a new user in the system.
   * 
   * @param request Registration request containing user details
   * @return AuthResponse containing user information and tokens
   * @throws ValidationException if email or username is already taken
   */
  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    log.info("Processing registration request for email: {}", request.getEmail());

    validateRegistrationRequest(request);

    Auth auth = createNewUser(request);
    sendVerificationEmail(auth);

    return generateAuthResponse(auth);
  }

  /**
   * Authenticates a user and generates access tokens.
   * 
   * @param request Login request containing credentials
   * @return AuthResponse containing user information and tokens
   * @throws AuthenticationException if credentials are invalid or email is not
   *                                 verified
   */
  @Override
  @Transactional
  public AuthResponse login(LoginRequest request) {
    log.info("Processing login request for email: {}", request.getEmail());

    Auth auth = authenticateUser(request);
    updateLastLogin(auth);

    return generateAuthResponse(auth);
  }

  /**
   * Retrieves user profile information.
   * 
   * @param userId ID of the user
   * @return ProfileResponse containing user profile details
   * @throws UserNotFoundException if user is not found
   */
  @Override
  @Transactional(readOnly = true)
  public ProfileResponse getProfile(UUID userId) {
    log.info("Fetching profile for user: {}", userId);

    Auth auth = findUserById(userId);
    return authMapper.toProfileResponse(auth);
  }

  /**
   * Updates user profile information.
   * 
   * @param userId  ID of the user
   * @param request Registration request containing updated information
   * @return ProfileResponse containing updated user profile
   * @throws UserNotFoundException if user is not found
   */
  @Override
  @Transactional
  public ProfileResponse updateProfile(UUID userId, RegisterRequest request) {
    log.info("Updating profile for user: {}", userId);

    Auth auth = findUserById(userId);
    updateUserDetails(auth, request);

    return authMapper.toProfileResponse(auth);
  }

  /**
   * Initiates the password reset process.
   * 
   * @param email Email of the user
   * @throws UserNotFoundException if user is not found
   */
  @Override
  @Transactional
  public void initiatePasswordReset(String email) {
    log.info("Initiating password reset for email: {}", email);

    Auth auth = findUserByEmail(email);
    String resetToken = generateResetToken();
    updatePasswordResetToken(auth, resetToken);

    sendPasswordResetEmail(email, resetToken);
  }

  /**
   * Resets the user's password using a reset token.
   * 
   * @param request Password reset request containing token and new password
   * @throws TokenException if token is invalid or expired
   */
  @Override
  @Transactional
  public void resetPassword(PasswordResetRequest request) {
    log.info("Processing password reset request");

    Auth auth = findUserByResetToken(request.getToken());
    validateResetToken(auth);
    updatePassword(auth, request.getNewPassword());

    sendPasswordChangedEmail(auth.getEmail());
  }

  /**
   * Verifies user's email using verification token.
   * 
   * @param token Email verification token
   * @throws TokenException if token is invalid
   */
  @Override
  @Transactional
  public void verifyEmail(String token) {
    log.info("Verifying email with token");

    Auth auth = findUserByVerificationToken(token);
    verifyUserEmail(auth);

    sendWelcomeEmail(auth.getEmail());
  }

  /**
   * Resends email verification token.
   * 
   * @param email Email of the user
   * @throws UserNotFoundException if user is not found
   * @throws ValidationException   if email is already verified
   */
  @Override
  @Transactional
  public void resendVerificationEmail(String email) {
    log.info("Resending verification email to: {}", email);

    Auth auth = findUserByEmail(email);
    validateEmailForResend(auth);

    String newToken = generateVerificationToken();
    updateVerificationToken(auth, newToken);

    sendVerificationEmail(auth);
  }

  /**
   * Deletes a user account.
   * 
   * @param userId ID of the user
   * @throws UserNotFoundException if user is not found
   */
  @Override
  @Transactional
  public void deleteAccount(UUID userId) {
    log.info("Deleting account for user: {}", userId);

    Auth auth = findUserById(userId);
    sendAccountDeletionEmail(auth.getEmail());
    authRepository.delete(auth);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isUsernameAvailable(String username) {
    return !authRepository.existsByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isEmailAvailable(String email) {
    return !authRepository.existsByEmail(email);
  }

  // Private helper methods

  private void validateRegistrationRequest(RegisterRequest request) {
    if (!isEmailAvailable(request.getEmail())) {
      throw ValidationException.emailAlreadyExists();
    }
    if (!isUsernameAvailable(request.getUsername())) {
      throw ValidationException.usernameAlreadyExists();
    }
  }

  private Auth createNewUser(RegisterRequest request) {
    Auth auth = authMapper.toEntity(request);
    auth.setPassword(passwordEncoder.encode(request.getPassword()));
    auth.setEmailVerificationToken(generateVerificationToken());
    return authRepository.save(auth);
  }

  private Auth authenticateUser(LoginRequest request) {
    Auth auth = findUserByEmail(request.getEmail());
    if (!passwordEncoder.matches(request.getPassword(), auth.getPassword())) {
      throw AuthenticationException.invalidCredentials();
    }
    if (!auth.isEmailVerified()) {
      throw AuthenticationException.emailNotVerified();
    }
    return auth;
  }

  private void updateLastLogin(Auth auth) {
    auth.setLastLogin(LocalDateTime.now());
    authRepository.save(auth);
  }

  private AuthResponse generateAuthResponse(Auth auth) {
    var tokenResponse = tokenService.generateTokens(auth.getId());
    var authResponse = authMapper.toAuthResponse(auth);
    return authMapper.updateAuthResponseWithTokens(tokenResponse, authResponse);
  }

  private Auth findUserById(UUID userId) {
    return authRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byId(userId.toString()));
  }

  private Auth findUserByEmail(String email) {
    return authRepository.findByEmail(email)
        .orElseThrow(() -> UserNotFoundException.byEmail(email));
  }

  private void updateUserDetails(Auth auth, RegisterRequest request) {
    authMapper.updateEntityFromRequest(request, auth);
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      auth.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    authRepository.save(auth);
  }

  private void updatePasswordResetToken(Auth auth, String resetToken) {
    auth.setPasswordResetToken(resetToken);
    auth.setPasswordResetTokenExpiration(LocalDateTime.now().plusHours(PASSWORD_RESET_TOKEN_EXPIRY_HOURS));
    authRepository.save(auth);
  }

  private Auth findUserByResetToken(String token) {
    return authRepository.findByPasswordResetToken(token)
        .orElseThrow(TokenException::tokenNotFound);
  }

  private void validateResetToken(Auth auth) {
    if (auth.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
      throw TokenException.expiredToken();
    }
  }

  private void updatePassword(Auth auth, String newPassword) {
    auth.setPassword(passwordEncoder.encode(newPassword));
    auth.setPasswordResetToken(null);
    auth.setPasswordResetTokenExpiration(null);
    authRepository.save(auth);
  }

  private Auth findUserByVerificationToken(String token) {
    return authRepository.findByEmailVerificationToken(token)
        .orElseThrow(TokenException::tokenNotFound);
  }

  private void verifyUserEmail(Auth auth) {
    auth.setEmailVerified(true);
    auth.setEmailVerificationToken(null);
    authRepository.save(auth);
  }

  private void validateEmailForResend(Auth auth) {
    if (auth.isEmailVerified()) {
      throw ValidationException.invalidEmail();
    }
  }

  private void updateVerificationToken(Auth auth, String newToken) {
    auth.setEmailVerificationToken(newToken);
    authRepository.save(auth);
  }

  // Email notification methods
  private void sendVerificationEmail(Auth auth) {
    // TODO: Implement Kafka email notification
    // kafkaTemplate.send(EMAIL_TOPIC, new EmailNotification(
    // auth.getEmail(),
    // "Verify your email",
    // "Please verify your email using token: " + auth.getEmailVerificationToken()
    // ));
  }

  private void sendPasswordResetEmail(String email, String resetToken) {
    // TODO: Implement Kafka email notification
    // kafkaTemplate.send(EMAIL_TOPIC, new EmailNotification(
    // email,
    // "Password Reset Request",
    // "Use this token to reset your password: " + resetToken
    // ));
  }

  private void sendPasswordChangedEmail(String email) {
    // TODO: Implement Kafka email notification
    // kafkaTemplate.send(EMAIL_TOPIC, new EmailNotification(
    // email,
    // "Password Changed",
    // "Your password has been successfully changed"
    // ));
  }

  private void sendWelcomeEmail(String email) {
    // TODO: Implement Kafka email notification
    // kafkaTemplate.send(EMAIL_TOPIC, new EmailNotification(
    // email,
    // "Welcome to Jobber",
    // "Your email has been verified. Welcome to our platform!"
    // ));
  }

  private void sendAccountDeletionEmail(String email) {
    // TODO: Implement Kafka email notification
    // kafkaTemplate.send(EMAIL_TOPIC, new EmailNotification(
    // email,
    // "Account Deleted",
    // "Your account has been successfully deleted"
    // ));
  }

  private String generateVerificationToken() {
    return UUID.randomUUID().toString();
  }

  private String generateResetToken() {
    return UUID.randomUUID().toString();
  }
}
