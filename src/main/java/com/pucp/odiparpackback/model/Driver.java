package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "driver_id")
public class Driver extends Person {
  @Column(name = "license")
  private String license;

  @OneToOne(mappedBy = "driver")
  private Truck truck;

}