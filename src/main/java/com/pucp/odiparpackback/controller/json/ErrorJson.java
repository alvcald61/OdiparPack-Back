package com.pucp.odiparpackback.controller.json;

import lombok.Data;

@Data
public class ErrorJson {
  private final String message;

  public ErrorJson(String message) {
    this.message = message;
  }
}
