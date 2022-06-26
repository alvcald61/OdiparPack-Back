package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
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

  private String code;

  @OneToOne
  @JoinColumn(name = "driver_id")
  private Driver driver;

  @Column(name = "capacity", nullable = false)
  private Integer capacity;

  @Column(name = "plate")
  private String plate;

  @OneToMany(mappedBy = "truck")
  private Set<Breakdown> breakdowns = new LinkedHashSet<>();

  @Enumerated
  @Column(name = "status")
  private TruckStatus status;

  @ManyToOne
  @JoinColumn(name = "current_city_id")
  private City currentCity;

  @OneToMany
  @JoinColumn(name = "plan_id")
  private List<TransportationPlan> transportationPlanList;

  @OneToOne(mappedBy = "truck")
  private Maintenance maintenance;
}