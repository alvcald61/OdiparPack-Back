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
public class DepotAlgorithmResponse {
  @JsonProperty("ubigeo")
  private String ubigeo;

  @JsonProperty("city")
  private String city;

  @JsonProperty("truckList")
  private List<TruckAlgorithmResponse> truckList;

  @JsonProperty("packagesRouted")
  private Integer packagesRouted;

  @JsonProperty("depotCost")
  private Double depotCost;
}
