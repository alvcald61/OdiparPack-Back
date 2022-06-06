package com.pucp.odiparpackback.request;

import lombok.*;

@Data
public class MaintenanceRequest {
  
  private Long id;

  private String initialDate;

  private String finalDate;

}