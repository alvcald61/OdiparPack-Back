package com.pucp.odiparpackback.dto;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
public class MaintenanceDto {
  
  private Long id;

  private String initialDate;

  private String finalDate;

}