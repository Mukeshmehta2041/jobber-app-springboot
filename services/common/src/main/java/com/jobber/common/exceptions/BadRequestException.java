package com.jobber.common.exceptions;

public class BadRequestException extends BaseException {
  public BadRequestException(String message) {
    super(message, "BAD_REQUEST");
  }
}
