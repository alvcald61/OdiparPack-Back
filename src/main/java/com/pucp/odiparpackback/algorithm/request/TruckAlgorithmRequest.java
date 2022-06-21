package com.pucp.odiparpackback.algorithm.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TruckAlgorithmRequest {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("ubigeo")
  private String ubigeo;

  @JsonProperty("maxLoad")
  private Integer maxLoad;
}
