package com.pucp.odiparpackback.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TruckDto {
  private Long id;

  @NotNull
  private String ubigeo;

  private Double capacity;

  private String plate;

  private Boolean available;

}