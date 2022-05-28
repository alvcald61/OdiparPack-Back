package com.pucp.odiparpackback.service.json;

import com.pucp.odiparpackback.dto.TruckDto;
import lombok.Data;

import java.util.List;

@Data
public class AlgorithmServerRequestJson {
  private List<AlgorithmRequestJson> orderList;
  private List<TruckDto> truckList;
}
