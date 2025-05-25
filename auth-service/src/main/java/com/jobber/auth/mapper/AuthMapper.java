package com.jobber.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.jobber.auth.dto.request.RegisterRequest;
import com.jobber.auth.dto.response.AuthResponse;
import com.jobber.auth.dto.response.ProfileResponse;
import com.jobber.auth.dto.response.TokenResponse;
import com.jobber.auth.entities.Auth;

/**
 * Mapper interface for converting between Auth entity and DTOs.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMapper {

  /**
   * Converts RegisterRequest to Auth entity.
   * 
   * @param request Registration request DTO
   * @return Auth entity
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "emailVerified", constant = "false")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "lastLogin", ignore = true)
  @Mapping(target = "profilePicture", ignore = true)
  @Mapping(target = "profilePublicId", ignore = true)
  @Mapping(target = "emailVerificationToken", ignore = true)
  @Mapping(target = "browserName", ignore = true)
  @Mapping(target = "deviceType", ignore = true)
  @Mapping(target = "otp", ignore = true)
  @Mapping(target = "otpExpiration", ignore = true)
  @Mapping(target = "passwordResetToken", ignore = true)
  @Mapping(target = "passwordResetTokenExpiration", ignore = true)
  Auth toEntity(RegisterRequest request);

  /**
   * Converts Auth entity to AuthResponse.
   * 
   * @param auth Auth entity
   * @return AuthResponse DTO
   */
  @Mapping(target = "accessToken", ignore = true)
  @Mapping(target = "refreshToken", ignore = true)
  AuthResponse toAuthResponse(Auth auth);

  /**
   * Converts Auth entity to ProfileResponse.
   * 
   * @param auth Auth entity
   * @return ProfileResponse DTO
   */
  ProfileResponse toProfileResponse(Auth auth);

  /**
   * Updates Auth entity with values from RegisterRequest.
   * 
   * @param request Registration request DTO
   * @param auth    Auth entity to update
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "emailVerified", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "lastLogin", ignore = true)
  @Mapping(target = "profilePicture", ignore = true)
  @Mapping(target = "profilePublicId", ignore = true)
  @Mapping(target = "emailVerificationToken", ignore = true)
  @Mapping(target = "browserName", ignore = true)
  @Mapping(target = "deviceType", ignore = true)
  @Mapping(target = "otp", ignore = true)
  @Mapping(target = "otpExpiration", ignore = true)
  @Mapping(target = "passwordResetToken", ignore = true)
  @Mapping(target = "passwordResetTokenExpiration", ignore = true)
  void updateEntityFromRequest(RegisterRequest request, @MappingTarget Auth auth);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "country", ignore = true)
  @Mapping(target = "profilePicture", ignore = true)
  @Mapping(target = "emailVerified", ignore = true)
  @Mapping(target = "lastLogin", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  AuthResponse updateAuthResponseWithTokens(TokenResponse tokenResponse, @MappingTarget AuthResponse authResponse);
}