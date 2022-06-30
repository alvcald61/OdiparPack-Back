package com.pucp.odiparpackback.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
  //cityUnigeo
  @JsonProperty("routeFinish")
  private String routeFinish;

  private CityResponse cityFinish;

  private Double speed;
  //cityUnigeo
  //add velocity

}
