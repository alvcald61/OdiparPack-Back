package com.pucp.odiparpackback.service.json;

import com.pucp.odiparpackback.request.TruckRequest;
import lombok.Data;

import java.util.List;

@Data
public class AlgorithmServerRequestJson {
  private List<AlgorithmRequestJson> orderList;
  private List<TruckRequest> truckList;
}
