package com.pucp.odiparpackback.algorithm.request;

import lombok.Data;

@Data
public class OrderAlgorithmRequest {
  private Long id;
  private String ubigeo;
  private Integer packages;
  private Double remainingTime;
}
