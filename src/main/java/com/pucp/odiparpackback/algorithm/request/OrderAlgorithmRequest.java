package com.pucp.odiparpackback.algorithm.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAlgorithmRequest {
  private Long id;
  private String ubigeo;
  private Integer packages;
  private Double remainingTime;
}
