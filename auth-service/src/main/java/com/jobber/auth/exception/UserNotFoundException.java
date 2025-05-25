package com.jobber.auth.exception;

/**
 * Exception thrown when a user cannot be found in the system.
 */
public class UserNotFoundException extends AuthException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public static UserNotFoundException byId(String id) {
    return new UserNotFoundException("User not found with id: " + id);
  }

  public static UserNotFoundException byEmail(String email) {
    return new UserNotFoundException("User not found with email: " + email);
  }

  public static UserNotFoundException byUsername(String username) {
    return new UserNotFoundException("User not found with username: " + username);
  }
}