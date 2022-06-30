package com.pucp.odiparpackback.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CityResponse {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("longitude")
  private Double longitude;

  @JsonProperty("latitude")
  private Double latitude;

  @JsonProperty("ubigeo")
  private String ubigeo;

  @JsonProperty("region")
  private String region;
}
