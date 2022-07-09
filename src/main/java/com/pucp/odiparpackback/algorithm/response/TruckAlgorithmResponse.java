package com.pucp.odiparpackback.algorithm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TruckAlgorithmResponse {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("load")
  private Double load;

  @JsonProperty("cost")
  private Double cost;

  @JsonProperty("orderList")
  private List<SubOrderResponse> orderList;

  @JsonProperty("nodeRoute")
  private List<NodeAlgorithmResponse> nodeRoute;
}
