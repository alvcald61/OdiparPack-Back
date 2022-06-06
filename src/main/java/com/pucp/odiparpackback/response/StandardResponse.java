package com.pucp.odiparpackback.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StandardResponse<T> {
  private ErrorResponse error;
  private T data;
  private HttpStatus status;

  public StandardResponse(ErrorResponse error) {
    this.error = error;
    this.data = null;
  }

  public StandardResponse(ErrorResponse error, HttpStatus status) {
    this.error = error;
    this.data = null;
    this.status = status;
  }

  public StandardResponse(T data) {
    this.data = data;
    this.error = null;
    this.status = HttpStatus.OK;
  }
}
