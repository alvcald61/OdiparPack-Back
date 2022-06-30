package com.pucp.odiparpackback.request;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class TransportationPlanRequest {
  
  private Long id;
  
  private ProductOrderRequest productOrder;

  private DepotRequest depot;

  private TruckRequest truck;

}