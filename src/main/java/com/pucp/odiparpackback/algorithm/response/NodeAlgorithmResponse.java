package com.pucp.odiparpackback.algorithm.response;

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
public class NodeAlgorithmResponse {
  @JsonProperty("idOrder")
  private Long idOrder;

  @JsonProperty("ubigeo")
  private String ubigeo;

  @JsonProperty("travelCost")
  private Double travelCost;
}
