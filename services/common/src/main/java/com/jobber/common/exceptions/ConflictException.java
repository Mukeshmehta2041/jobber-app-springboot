package com.jobber.common.exceptions;

public class ConflictException extends BaseException {
  public ConflictException(String message) {
    super(message, "CONFLICT");
  }
}