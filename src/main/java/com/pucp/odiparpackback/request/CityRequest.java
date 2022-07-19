package com.pucp.odiparpackback.request;

import lombok.Data;


@Data
public class CityRequest {

  private Long id;

  private String name;

  private String longitude;

  private String latitude;

  private String ubigeo;

  private String region;

  private Integer ordersDeliveredCounter;
}