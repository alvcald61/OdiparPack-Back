package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Maintenance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Temporal(TemporalType.DATE)
  @Column(name = "initial_date", nullable = false)
  private Date initialDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "final_date", nullable = false)
  private Date finalDate;

}