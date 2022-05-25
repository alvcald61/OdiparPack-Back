package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.City;
import lombok.*;

import javax.persistence.*;

@Data
public class RouteDto {
  
  private Long id;
  
  private CityDto fromCity;
  
  private CityDto toCity;
  
  private Double distance;
  
  private Double speed;

}