package com.pucp.odiparpackback.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderRequest {

  @JsonProperty("orderId")
  private Long orderId;

  @NotNull
  @JsonProperty("amount")
  private Double amount;

  @NotNull
  @JsonProperty("destinationUbigeo")
  private String destinationUbigeo;

  @JsonProperty("clientId")
  private Long clientId;

}
