package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Truck {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToOne
  @JoinColumn(name = "driver_id")
  private Driver driver;

  @Column(name = "capacity", nullable = false)
  private Double capacity;

  @Column(name = "plate")
  private String plate;

  @OneToMany(mappedBy = "truck", orphanRemoval = true)
  private Set<Breakdown> breakdowns = new LinkedHashSet<>();

}