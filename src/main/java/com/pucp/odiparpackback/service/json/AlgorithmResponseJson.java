package com.pucp.odiparpackback.service.json;

import lombok.Data;

import java.util.List;
@Data
public class AlgorithmResponseJson {
  private Long ubigeo;
  private String city;
  private String amount;
  private List<TruckJson> truckList;
  private Double deportCost;
}
