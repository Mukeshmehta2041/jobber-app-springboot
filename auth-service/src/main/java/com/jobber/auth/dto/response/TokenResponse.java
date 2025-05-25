package com.jobber.auth.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for token responses.
 * Contains access and refresh tokens.
 */
@Data
@Builder
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}