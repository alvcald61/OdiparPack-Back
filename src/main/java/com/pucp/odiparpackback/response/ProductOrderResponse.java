package com.pucp.odiparpackback.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pucp.odiparpackback.request.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("registryDate")
    private String registryDate;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("state")
    private String state;

    @JsonProperty("maxDeliveryDate")
    private String maxDeliveryDate;

    @JsonProperty("client")
    private ClientResponse client;

    @JsonProperty("destination")
    private CityResponse destination;

}
