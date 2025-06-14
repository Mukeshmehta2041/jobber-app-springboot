package com.jobber.common.exceptions;

public class ForbiddenException extends BaseException {
  public ForbiddenException(String message) {
    super(message, "FORBIDDEN");
  }
}