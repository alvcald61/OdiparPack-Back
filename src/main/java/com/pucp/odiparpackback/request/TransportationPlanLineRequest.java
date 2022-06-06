package com.pucp.odiparpackback.request;

import lombok.Data;

@Data
public class TransportationPlanLineRequest {

  private Long id;

  private String arrivalDate;

  private CityRequest city;

  private TransportationPlanRequest transportationPlan;

}