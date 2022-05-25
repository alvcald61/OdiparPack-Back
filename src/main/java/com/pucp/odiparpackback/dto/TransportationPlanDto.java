package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.Depot;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlanLine;
import com.pucp.odiparpackback.model.Truck;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class TransportationPlanDto {
  
  private Long id;
  
  private ProductOrderDto productOrder;
  
  private Set<TransportationPlanLineDto> transportationPlanLines = new LinkedHashSet<>();

  private DepotDto depot;

  private TruckDto truck;

}