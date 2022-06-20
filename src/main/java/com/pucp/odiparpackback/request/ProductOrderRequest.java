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

  @JsonProperty("state")
  private String state;

  @NotNull
  @JsonProperty("maxDeliveryDate")
  private String maxDeliveryDate;

  @NotNull
  @JsonProperty("registryDate")
  private String registryDate;

  @NotNull
  @JsonProperty("destinationId")
  private Long destinationId;

  @JsonProperty("clientId")
  private Long clientId;

}
