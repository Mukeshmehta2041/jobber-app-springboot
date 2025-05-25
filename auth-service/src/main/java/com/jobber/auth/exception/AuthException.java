package com.jobber.auth.exception;

/**
 * Base exception class for all authentication-related exceptions.
 */
public class AuthException extends RuntimeException {

  public AuthException(String message) {
    super(message);
  }

  public AuthException(String message, Throwable cause) {
    super(message, cause);
  }
}