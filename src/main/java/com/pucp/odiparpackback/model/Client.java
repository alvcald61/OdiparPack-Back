package com.pucp.odiparpackback.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@PrimaryKeyJoinColumn(name = "client_id")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client extends Person {
  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "client", orphanRemoval = true)
  private Set<ProductOrder> productOrders = new LinkedHashSet<>();

}