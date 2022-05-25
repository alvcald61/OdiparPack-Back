package com.pucp.odiparpackback.dto;

import com.pucp.odiparpackback.enums.Region;
import com.pucp.odiparpackback.model.Client;
import com.pucp.odiparpackback.model.Route;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
public class CityDto {
  
  private Long id;  
  
  private String name;
  
  private String longitude;
  
  private String latitude;
  
  private String ubigeo;
  
  private Region region;

  private Set<Client> clients = new LinkedHashSet<>();

  private Set<Route> fromRoutes = new LinkedHashSet<>();
}