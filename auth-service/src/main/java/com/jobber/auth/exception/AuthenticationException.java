package com.jobber.auth.exception;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends AuthException {

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public static AuthenticationException invalidCredentials() {
    return new AuthenticationException("Invalid username or password");
  }

  public static AuthenticationException accountLocked() {
    return new AuthenticationException("Account is locked. Please try again later");
  }

  public static AuthenticationException accountDisabled() {
    return new AuthenticationException("Account is disabled");
  }

  public static AuthenticationException emailNotVerified() {
    return new AuthenticationException("Email is not verified");
  }
}