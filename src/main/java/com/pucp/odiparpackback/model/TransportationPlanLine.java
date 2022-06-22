package com.pucp.odiparpackback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransportationPlanLine {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Temporal(TemporalType.DATE)
  @Column(name = "arrival_date")
  private Date arrivalDate;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @ManyToOne
  @JoinColumn(name = "transportation_plan_id")
  private TransportationPlan transportationPlan;

}