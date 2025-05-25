package com.jobber.auth.exception;

import java.util.List;
import java.util.ArrayList;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends AuthException {

  private final List<String> errors;

  public ValidationException(String message) {
    super(message);
    this.errors = new ArrayList<>();
    this.errors.add(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
    this.errors = new ArrayList<>();
    this.errors.add(message);
  }

  public ValidationException(List<String> errors) {
    super("Validation failed: " + String.join(", ", errors));
    this.errors = new ArrayList<>(errors);
  }

  public List<String> getErrors() {
    return errors;
  }

  public static ValidationException usernameAlreadyExists() {
    return new ValidationException("Username is already taken");
  }

  public static ValidationException emailAlreadyExists() {
    return new ValidationException("Email is already registered");
  }

  public static ValidationException invalidPassword() {
    return new ValidationException("Password does not meet requirements");
  }

  public static ValidationException invalidEmail() {
    return new ValidationException("Invalid email format");
  }

  public static ValidationException invalidUsername() {
    return new ValidationException("Invalid username format");
  }
}