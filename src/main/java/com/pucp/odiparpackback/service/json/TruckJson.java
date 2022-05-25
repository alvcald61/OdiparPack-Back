package com.pucp.odiparpackback.service.json;

import lombok.Data;

import java.util.List;

@Data
public class TruckJson {
  private Long id;
  private Double load;
  private Double cost;
  private List<NodeRouteJson> nodeRoute;
}
