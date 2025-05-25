package com.jobber.auth.service.impl;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobber.auth.dto.response.TokenResponse;
import com.jobber.auth.entities.RefreshToken;
import com.jobber.auth.exception.TokenException;
import com.jobber.auth.repository.TokenRepository;
import com.jobber.auth.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the TokenService interface.
 * Handles JWT token generation, validation, and management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.access-token.expiration}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshTokenExpiration;

  private final TokenRepository tokenRepository;

  private Key getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Generates access and refresh tokens for a user.
   *
   * @param userId The ID of the user
   * @return TokenResponse containing both tokens
   */
  @Override
  @Transactional
  public TokenResponse generateTokens(UUID userId) {
    log.debug("Generating tokens for user: {}", userId);

    String accessToken = generateAccessToken(userId);
    String refreshToken = generateRefreshToken(userId);

    // Store refresh token in database
    RefreshToken refreshTokenEntity = RefreshToken.builder()
        .token(refreshToken)
        .userId(userId)
        .expiryDate(LocalDateTime.now().plusMinutes(refreshTokenExpiration))
        .revoked(false)
        .build();
    tokenRepository.save(refreshTokenEntity);

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  /**
   * Generates a new access token using a refresh token.
   *
   * @param refreshToken The refresh token
   * @return TokenResponse containing new access token
   * @throws TokenException if refresh token is invalid or expired
   */
  @Override
  @Transactional
  public TokenResponse refreshAccessToken(String refreshToken) {
    log.debug("Refreshing access token");

    try {
      Claims claims = validateToken(refreshToken);
      String userId = claims.getSubject();

      // Verify refresh token exists and is not revoked
      RefreshToken storedToken = tokenRepository.findByToken(refreshToken)
          .orElseThrow(TokenException::tokenNotFound);

      if (storedToken.isRevoked()) {
        throw TokenException.revokedToken();
      }

      if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw TokenException.expiredToken();
      }

      String newAccessToken = generateAccessToken(UUID.fromString(userId));

      return TokenResponse.builder()
          .accessToken(newAccessToken)
          .refreshToken(refreshToken)
          .build();
    } catch (Exception e) {
      log.error("Failed to refresh access token: {}", e.getMessage());
      throw TokenException.invalidToken();
    }
  }

  /**
   * Validates an access token.
   *
   * @param token The access token to validate
   * @throws TokenException if token is invalid or expired
   */
  @Override
  public void validateAccessToken(String token) {
    try {
      Claims claims = validateToken(token);
      if (!"access".equals(claims.get("type"))) {
        throw TokenException.invalidToken();
      }
    } catch (Exception e) {
      log.error("Access token validation failed: {}", e.getMessage());
      throw TokenException.invalidToken();
    }
  }

  /**
   * Validates a refresh token.
   *
   * @param token The refresh token to validate
   * @throws TokenException if token is invalid, expired, or revoked
   */
  @Override
  @Transactional
  public void validateRefreshToken(String token) {
    try {
      Claims claims = validateToken(token);
      if (!"refresh".equals(claims.get("type"))) {
        throw TokenException.invalidToken();
      }

      RefreshToken storedToken = tokenRepository.findByToken(token)
          .orElseThrow(TokenException::tokenNotFound);

      if (storedToken.isRevoked()) {
        throw TokenException.revokedToken();
      }

      if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw TokenException.expiredToken();
      }
    } catch (Exception e) {
      log.error("Refresh token validation failed: {}", e.getMessage());
      throw TokenException.invalidToken();
    }
  }

  /**
   * Extracts user ID from a token.
   *
   * @param token The token to extract user ID from
   * @return The user ID
   * @throws TokenException if token is invalid
   */
  @Override
  public String getUserIdFromToken(String token) {
    try {
      Claims claims = validateToken(token);
      return claims.getSubject();
    } catch (Exception e) {
      log.error("Failed to extract user ID from token: {}", e.getMessage());
      throw TokenException.invalidToken();
    }
  }

  /**
   * Revokes a refresh token.
   *
   * @param token The refresh token to revoke
   */
  @Override
  @Transactional
  public void revokeRefreshToken(String token) {
    log.debug("Revoking refresh token");
    tokenRepository.findByToken(token)
        .ifPresent(refreshToken -> {
          refreshToken.setRevoked(true);
          tokenRepository.save(refreshToken);
        });
  }

  /**
   * Revokes all tokens for a user.
   *
   * @param userId The ID of the user
   */
  @Override
  @Transactional
  public void revokeAllTokens(UUID userId) {
    log.debug("Revoking all tokens for user: {}", userId);
    tokenRepository.findByUserId(userId).forEach(refreshToken -> {
      refreshToken.setRevoked(true);
      tokenRepository.save(refreshToken);
    });
  }

  /**
   * Validates a JWT token.
   *
   * @param token The token to validate
   * @return Claims from the token if valid
   * @throws TokenException if token is invalid or expired
   */
  private Claims validateToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      log.error("Token validation failed: {}", e.getMessage());
      throw TokenException.invalidToken();
    }
  }

  private String generateAccessToken(UUID userId) {
    return generateToken(userId.toString(), accessTokenExpiration, "access");
  }

  private String generateRefreshToken(UUID userId) {
    return generateToken(userId.toString(), refreshTokenExpiration, "refresh");
  }

  private String generateToken(String userId, long expiration, String type) {
    Instant now = Instant.now();
    Instant expiryDate = now.plus(expiration, ChronoUnit.MINUTES);

    Map<String, Object> claims = new HashMap<>();
    claims.put("type", type);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userId)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiryDate))
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }
}