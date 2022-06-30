package com.pucp.odiparpackback.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pucp.odiparpackback.utils.BreakdownType;
import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BreakdownRequest {
  @JsonProperty("id")
  private Long id;

  @NotNull
  @JsonProperty("breakdownType")
  private BreakdownType breakdownType;

  @NotNull
  @JsonProperty("solutionAction")
  private String solutionAction;

  @JsonProperty("maintenance")
  private Maintenance maintenance;

  @NotNull
  @JsonProperty("truck")
  private Truck truck;

}