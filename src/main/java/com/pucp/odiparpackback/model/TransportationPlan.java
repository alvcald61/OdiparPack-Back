package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.utils.Speed;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransportationPlan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToOne
  @JoinColumn(name = "order_id")
  private ProductOrder order;

  @Column(name = "routeStart")
  private Date routeStart;

  @Column(name = "routeFinish")
  private Date routeFinish;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @Column(name = "speed")
  private Speed speed;
}