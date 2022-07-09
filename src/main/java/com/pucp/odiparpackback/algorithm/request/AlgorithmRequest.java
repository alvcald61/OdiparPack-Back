package com.pucp.odiparpackback.algorithm.request;

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
public class AlgorithmRequest {
  private List<OrderAlgorithmRequest> orderList;
  private List<TruckAlgorithmRequest> truckList;
  private List<BlockadeAlgorithmRequest> blockadeList;
}
