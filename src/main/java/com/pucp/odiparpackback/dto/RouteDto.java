package com.pucp.odiparpackback.dto;

import lombok.Data;

@Data
public class RouteDto {

  private Long id;

  private CityDto fromCity;

  private CityDto toCity;

  private Double distance;

  private Double speed;

}