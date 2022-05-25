package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Person;
import com.pucp.odiparpackback.model.ProductOrder;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
public class ClientDto {
  
  private CityDto city;
  
  private Set<ProductOrderDto> productOrders ;

}