package com.jobber.auth.service;

import java.util.UUID;

import com.jobber.auth.dto.response.TokenResponse;

/**
 * Service interface for handling JWT token operations.
 * Provides methods for token generation, validation, and management.
 */
public interface TokenService {

  /**
   * Generates access and refresh tokens for a user.
   *
   * @param userId The ID of the user
   * @return TokenResponse containing both tokens
   */
  TokenResponse generateTokens(UUID userId);

  /**
   * Generates a new access token using a refresh token.
   *
   * @param refreshToken The refresh token
   * @return TokenResponse containing new access token
   */
  TokenResponse refreshAccessToken(String refreshToken);

  /**
   * Validates an access token.
   *
   * @param token The access token to validate
   * @throws TokenException if token is invalid or expired
   */
  void validateAccessToken(String token);

  /**
   * Validates a refresh token.
   *
   * @param token The refresh token to validate
   * @throws TokenException if token is invalid, expired, or revoked
   */
  void validateRefreshToken(String token);

  /**
   * Extracts user ID from a token.
   *
   * @param token The token to extract user ID from
   * @return The user ID
   * @throws TokenException if token is invalid
   */
  String getUserIdFromToken(String token);

  /**
   * Revokes a refresh token.
   *
   * @param token The refresh token to revoke
   */
  void revokeRefreshToken(String token);

  /**
   * Revokes all tokens for a user.
   *
   * @param userId The ID of the user
   */
  void revokeAllTokens(UUID userId);
}