package com.pucp.odiparpackback.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TruckRequest {
  private Long id;

  @NotNull
  private String ubigeo;

  private Double capacity;

  private String code;

  private Boolean available;

}