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
public class RouteBlock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "route_id")
  private Route route;

  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;
}
