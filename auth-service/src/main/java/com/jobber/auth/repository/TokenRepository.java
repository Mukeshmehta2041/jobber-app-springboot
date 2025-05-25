package com.jobber.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobber.auth.entities.RefreshToken;

/**
 * Repository interface for RefreshToken entity persistence operations.
 */
@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, UUID> {

  /**
   * Finds a refresh token by its token value.
   * 
   * @param token Token value to search for
   * @return Optional containing the RefreshToken entity if found
   */
  Optional<RefreshToken> findByToken(String token);

  /**
   * Finds all refresh tokens for a specific user.
   * 
   * @param userId User ID to search for
   * @return List of RefreshToken entities
   */
  List<RefreshToken> findByUserId(UUID userId);

  /**
   * Deletes all refresh tokens for a specific user.
   * 
   * @param userId User ID
   */
  @Modifying
  @Query("DELETE FROM RefreshToken rt WHERE rt.userId = :userId")
  void deleteByUserId(@Param("userId") UUID userId);

  /**
   * Deletes expired refresh tokens.
   */
  @Modifying
  @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < CURRENT_TIMESTAMP")
  void deleteExpiredTokens();

  /**
   * Counts active refresh tokens for a user.
   * 
   * @param userId User ID
   * @return Number of active refresh tokens
   */
  @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.userId = :userId AND rt.expiryDate > CURRENT_TIMESTAMP")
  long countActiveTokensByUserId(@Param("userId") UUID userId);

  /**
   * Finds refresh tokens that will expire within a specified time period.
   * 
   * @param expiryThreshold Time threshold for expiry
   * @return List of RefreshToken entities
   */
  @Query("SELECT rt FROM RefreshToken rt WHERE rt.expiryDate BETWEEN CURRENT_TIMESTAMP AND :expiryThreshold")
  List<RefreshToken> findTokensExpiringWithin(@Param("expiryThreshold") LocalDateTime expiryThreshold);
}