package com.pucp.odiparpackback.controller.json;

import lombok.Data;

@Data
public class ResponseJson<T> {
  private ErrorJson error;
  private T data;

  public ResponseJson(ErrorJson error) {
    this.error = error;
    this.data = null;
  }

  public ResponseJson(T data) {
    this.data = data;
    this.error = null;
  }
}
