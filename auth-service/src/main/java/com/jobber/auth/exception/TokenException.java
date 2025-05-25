package com.jobber.auth.exception;

/**
 * Exception thrown when there are issues with tokens.
 */
public class TokenException extends AuthException {

  public TokenException(String message) {
    super(message);
  }

  public TokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public static TokenException invalidToken() {
    return new TokenException("Invalid token");
  }

  public static TokenException expiredToken() {
    return new TokenException("Token has expired");
  }

  public static TokenException revokedToken() {
    return new TokenException("Token has been revoked");
  }

  public static TokenException tokenNotFound() {
    return new TokenException("Token not found");
  }
}