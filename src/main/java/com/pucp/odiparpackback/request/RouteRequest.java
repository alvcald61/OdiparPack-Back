package com.pucp.odiparpackback.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteRequest {

  private Long id;

  private String UbigeoFrom;

  private String UbigeoTo;

  private Double distance;

  private Double speed;

}