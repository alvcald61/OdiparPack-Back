package com.pucp.odiparpackback.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteBlockListRequest {
  @NonNull
  @JsonProperty("startDate")
  private String startDate;

  @NonNull
  @JsonProperty("endDate")
  private String endDate;
}
