package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.Person;
import com.pucp.odiparpackback.model.Truck;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Data
public class DriverDto {
  
  private String license;
  
  private TruckDto truck;

}