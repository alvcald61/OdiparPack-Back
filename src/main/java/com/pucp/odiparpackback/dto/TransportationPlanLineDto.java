package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.TransportationPlan;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
public class TransportationPlanLineDto {
  
  private Long id;
  
  private String arrivalDate;

  private CityDto city;

  private TransportationPlanDto transportationPlan;

}