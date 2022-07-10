package com.pucp.odiparpackback.response;

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
public class TransportationPlanResponse {
  @JsonProperty("idTransportationPlan")
  private Long idTransportationPlan;

  @JsonProperty("order")
  private ProductOrderResponse order;

  @JsonProperty("routeStart")
  private String routeStart;

  @JsonProperty("routeFinish")
  private String routeFinish;

  @JsonProperty("city")
  private CityResponse city;

  @JsonProperty("speed")
  private Double speed;

  @JsonProperty("amount")
  private Integer amount;
}
