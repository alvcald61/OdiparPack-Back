package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.enums.OrderState;
import lombok.AllArgsConstructor;
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
public class ProductOrder {
  //mandar
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "registry_date")
  private Date registryDate;

  //mandar
  @Column(name = "amount", nullable = false)
  private Double amount;

  @Enumerated
  @Column(name = "state")
  private OrderState state;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "max_delivery_date")
  private Date maxDeliveryDate;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  //mandar
  @ManyToOne
  @JoinColumn(name = "destination_id")
  private City destination;

  @OneToOne(orphanRemoval = true)
  @JoinColumn(name = "transportation_plan_id")
  private TransportationPlan transportationPlan;

}