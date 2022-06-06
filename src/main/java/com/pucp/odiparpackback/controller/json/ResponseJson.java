package com.pucp.odiparpackback.controller.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseJson<T> {
  private ErrorJson error;
  private T data;
  private HttpStatus status;

  public ResponseJson(ErrorJson error) {
    this.error = error;
    this.data = null;
  }

  public ResponseJson(ErrorJson error, HttpStatus status) {
    this.error = error;
    this.data = null;
    this.status = status;
  }

  public ResponseJson(T data) {
    this.data = data;
    this.error = null;
    this.status = HttpStatus.OK;
  }
}
