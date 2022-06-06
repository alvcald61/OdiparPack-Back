package com.pucp.odiparpackback.request;

import com.pucp.odiparpackback.utils.BreakdownType;
import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BreakdownRequest {
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