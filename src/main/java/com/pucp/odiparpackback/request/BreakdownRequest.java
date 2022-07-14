package com.pucp.odiparpackback.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pucp.odiparpackback.utils.BreakdownType;
import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreakdownRequest {
  @NotNull
  @JsonProperty("breakdownType")
  private String breakdownType;

  @NotNull
  @JsonProperty("truckId")
  private Long truckId;

}