package com.pucp.odiparpackback.dto;

import lombok.Data;

@Data
public class TransportationPlanLineDto {

  private Long id;

  private String arrivalDate;

  private CityDto city;

  private TransportationPlanDto transportationPlan;

}