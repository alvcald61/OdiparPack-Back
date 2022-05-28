package com.pucp.odiparpackback.dto;

import lombok.Data;


@Data
public class CityDto {

  private Long id;

  private String name;

  private String longitude;

  private String latitude;

  private String ubigeo;

  private String region;
}