package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.City;
import lombok.*;

import javax.persistence.*;

@Data
public class DepotDto {
  private Long id;
  
  private CityDto city;

}