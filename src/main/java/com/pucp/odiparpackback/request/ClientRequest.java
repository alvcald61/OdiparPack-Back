package com.pucp.odiparpackback.request;

import lombok.Data;

import java.util.Set;


@Data
public class ClientRequest {

  private CityRequest city;

  private Set<ProductOrderRequest> productOrders;

}