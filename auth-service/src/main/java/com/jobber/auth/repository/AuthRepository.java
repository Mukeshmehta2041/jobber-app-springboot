package com.jobber.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

import com.jobber.auth.entities.Auth;

/**
 * Repository interface for Auth entity persistence operations.
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {

  /**
   * Finds a user by their email address.
   * 
   * @param email Email address to search for
   * @return Optional containing the Auth entity if found
   */
  Optional<Auth> findByEmail(String email);

  /**
   * Finds a user by their username.
   * 
   * @param username Username to search for
   * @return Optional containing the Auth entity if found
   */
  Optional<Auth> findByUsername(String username);

  /**
   * Finds a user by their email verification token.
   * 
   * @param token Email verification token
   * @return Optional containing the Auth entity if found
   */
  Optional<Auth> findByEmailVerificationToken(String token);

  /**
   * Finds a user by their password reset token.
   * 
   * @param token Password reset token
   * @return Optional containing the Auth entity if found
   */
  Optional<Auth> findByPasswordResetToken(String token);

  /**
   * Checks if a username exists.
   * 
   * @param username Username to check
   * @return true if username exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Checks if an email exists.
   * 
   * @param email Email to check
   * @return true if email exists, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Finds users by country.
   * 
   * @param country Country to search for
   * @return List of Auth entities
   */
  @Query("SELECT a FROM Auth a WHERE a.country = :country")
  List<Auth> findByCountry(@Param("country") String country);

  /**
   * Finds users by email verification status.
   * 
   * @param verified Email verification status
   * @return List of Auth entities
   */
  @Query("SELECT a FROM Auth a WHERE a.emailVerified = :verified")
  List<Auth> findByEmailVerified(@Param("verified") boolean verified);

  /**
   * Finds users by last login date range.
   * 
   * @param startDate Start date
   * @param endDate   End date
   * @return List of Auth entities
   */
  @Query("SELECT a FROM Auth a WHERE a.lastLogin BETWEEN :startDate AND :endDate")
  List<Auth> findByLastLoginBetween(@Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  /**
   * Deletes users by email verification status.
   * 
   * @param verified Email verification status
   */
  @Modifying
  @Query("DELETE FROM Auth a WHERE a.emailVerified = :verified")
  void deleteByEmailVerified(@Param("verified") boolean verified);
}