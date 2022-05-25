package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.model.Breakdown;
import com.pucp.odiparpackback.model.Driver;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class TruckDto {
  private Long id;

  private Driver driver;

  private Double capacity;

  private String plate;

  private Set<BreakdownDto> breakdowns = new LinkedHashSet<>();

}