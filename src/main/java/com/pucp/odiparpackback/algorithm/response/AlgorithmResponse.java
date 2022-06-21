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
public class AlgorithmResponse {
  @JsonProperty("depotList")
  private List<DepotAlgorithmResponse> depotList;
}
