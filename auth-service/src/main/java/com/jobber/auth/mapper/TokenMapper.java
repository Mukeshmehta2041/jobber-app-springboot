package com.jobber.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

import com.jobber.auth.dto.response.AuthResponse;
import com.jobber.auth.dto.response.TokenResponse;

/**
 * Mapper interface for handling token-related conversions.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TokenMapper {

  /**
   * Updates AuthResponse with token information.
   * 
   * @param tokenResponse Token response containing access and refresh tokens
   * @param authResponse  Auth response to update
   * @return Updated AuthResponse
   */
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