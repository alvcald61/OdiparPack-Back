package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.utils.BreakdownType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Breakdown {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Enumerated
  @Column(name = "breakdown_type")
  private BreakdownType breakdownType;

  @Column(name = "start_date")
  private Date startDate;

  @Column(name = "endDate")
  private Date endDate;

  @Column(name = "stop_latitude")
  private Double stopLatitude;

  @Column(name = "stop_longitude")
  private Double stopLongitude;

  @Column(name = "solution_action")
  private String solutionAction;

  @OneToOne
  @JoinColumn(name = "truck_id")
  private Truck truck;

}