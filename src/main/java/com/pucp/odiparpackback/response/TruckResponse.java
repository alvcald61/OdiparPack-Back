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
  private String code;
  private TruckStatus status;
  private CityResponse currentCity;
  private BreakdownResponse breakdownResponse;
  private Double longitude;
  private Double latitude;
  private List<TransportationPlanResponse> transportationPlanList;
}
