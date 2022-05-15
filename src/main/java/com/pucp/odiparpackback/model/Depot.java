package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Depot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;
  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

}