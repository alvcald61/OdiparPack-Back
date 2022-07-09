package com.pucp.odiparpackback.request;

import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TruckRequest {
  private Long id;

  @NotNull
  private String ubigeo;

  private Double capacity;

  private String code;
  //not in request, just taking advantage of what Alvaro did...
  private TruckStatus status;

}