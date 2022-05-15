package com.pucp.odiparpackback.exceptions;

import org.springframework.http.HttpStatus;

public class GenericCustomException extends RuntimeException {
  private HttpStatus status;
  public GenericCustomException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
