package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Maintenance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Temporal(TemporalType.DATE)
  @Column(name = "initial_date", nullable = false)
  private Date initialDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "truck_id")
  private Truck truck;

}