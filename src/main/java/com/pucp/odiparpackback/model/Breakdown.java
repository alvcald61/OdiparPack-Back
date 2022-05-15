package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.enums.BreakdownType;
import lombok.*;

import javax.persistence.*;


@Entity
public class Breakdown {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Enumerated
  @Column(name = "breakdown_type")
  private BreakdownType breakdownType;

  @Column(name = "solution_action")
  private String solutionAction;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "maintenance_id")
  private Maintenance maintenance;

  @ManyToOne
  @JoinColumn(name = "truck_id")
  private Truck truck;

}