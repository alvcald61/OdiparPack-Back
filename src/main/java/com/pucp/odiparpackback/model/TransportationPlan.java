package com.pucp.odiparpackback.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransportationPlan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToOne(mappedBy = "transportationPlan", orphanRemoval = true)
  private ProductOrder productOrder;

  @OneToMany(mappedBy = "transportationPlan", orphanRemoval = true)
  private Set<TransportationPlanLine> transportationPlanLines = new LinkedHashSet<>();

  @ManyToOne
  @JoinColumn(name = "depot_id")
  private Depot depot;

  @ManyToOne
  @JoinColumn(name = "truck_id")
  private Truck truck;

}