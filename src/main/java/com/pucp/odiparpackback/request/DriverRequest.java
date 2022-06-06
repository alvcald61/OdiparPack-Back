package com.pucp.odiparpackback.request;

import lombok.*;

@Data
public class DriverRequest {
  
  private String license;
  
  private TruckRequest truck;

}