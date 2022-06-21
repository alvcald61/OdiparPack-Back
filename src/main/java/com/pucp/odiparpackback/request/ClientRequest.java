package com.pucp.odiparpackback.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientRequest {
  @JsonProperty("id")
  private Long id;

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("ruc")
  private String ruc;
}