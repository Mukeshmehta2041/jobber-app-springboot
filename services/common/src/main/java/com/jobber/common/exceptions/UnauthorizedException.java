package com.jobber.common.exceptions;

public class UnauthorizedException extends BaseException {
  public UnauthorizedException(String message) {
    super(message, "UNAUTHORIZED");
  }
}
