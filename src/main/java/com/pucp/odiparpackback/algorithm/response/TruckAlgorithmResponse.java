package com.pucp.odiparpackback.algorithm.response;

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
  private Long id;
  private Double load;
  private Double cost;
  private List<Integer> orderList;
  private List<NodeAlgorithmResponse> nodeRoute;
}
