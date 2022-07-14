package com.pucp.odiparpackback.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pucp.odiparpackback.utils.BreakdownType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreakdownResponse {
  @JsonProperty("breakdownId")
  private Long breakdownId;

  @JsonProperty("type")
  private BreakdownType type;
}
