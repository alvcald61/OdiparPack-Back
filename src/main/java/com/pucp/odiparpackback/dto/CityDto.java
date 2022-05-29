package com.pucp.odiparpackback.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CityDto {

  private Long id;

  private String name;

  private String longitude;

  private String latitude;

  private String ubigeo;

  private String region;

  private List<RouteDto> connections = new ArrayList<>();
}