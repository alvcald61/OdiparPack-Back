package com.pucp.odiparpackback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteDto {

  private Long id;

  private String UbigeoFrom;

  private String UbigeoTo;

  private Double distance;

  private Double speed;

}