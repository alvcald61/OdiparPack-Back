package com.pucp.odiparpackback.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@PrimaryKeyJoinColumn(name = "client_id")
public class Client extends Person {
  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "client", orphanRemoval = true)
  private Set<ProductOrder> productOrders = new LinkedHashSet<>();

}