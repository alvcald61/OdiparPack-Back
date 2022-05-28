package com.pucp.odiparpackback.dto;

import lombok.Data;

import java.util.Set;


@Data
public class ClientDto {

  private CityDto city;

  private Set<ProductOrderDto> productOrders;

}