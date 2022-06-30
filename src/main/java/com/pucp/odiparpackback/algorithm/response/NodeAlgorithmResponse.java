package com.pucp.odiparpackback.algorithm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

  @JsonProperty("speed")
  private Double speed;
}
