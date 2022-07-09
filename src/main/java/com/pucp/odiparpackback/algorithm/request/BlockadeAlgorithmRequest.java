package com.pucp.odiparpackback.algorithm.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockadeAlgorithmRequest {

  @JsonProperty("firstNode")
  private String firstNode;

  @JsonProperty("secondNode")
  private String secondNode;

  @JsonProperty("start")
  private Double start;

  @JsonProperty("end")
  private Double end;
}
