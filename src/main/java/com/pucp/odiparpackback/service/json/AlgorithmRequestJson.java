package com.pucp.odiparpackback.service.json;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmRequestJson {
  private String ubigeo;
  private String amount;
  private List<TruckJson> truckList;
  private Double deportCost; 
}
