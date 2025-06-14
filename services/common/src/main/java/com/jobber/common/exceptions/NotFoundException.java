package com.jobber.common.exceptions;

public class NotFoundException extends BaseException {
  public NotFoundException(String message) {
    super(message, "NOT_FOUND");
  }
}
