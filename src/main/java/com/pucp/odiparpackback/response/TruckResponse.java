package com.pucp.odiparpackback.response;


import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TruckResponse {
  private Long truckId;
  private Integer capacity;
  private String plate;
  private TruckStatus status;
  private CityResponse currentCity;
  private List<TransportationPlanResponse> transportationPlanList;
}
