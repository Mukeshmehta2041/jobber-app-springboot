package com.jobber.common.exceptions;

public class ServiceUnavailableException extends BaseException {
  public ServiceUnavailableException(String message) {
    super(message, "SERVICE_UNAVAILABLE");
  }
}
