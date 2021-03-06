package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Route {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "from_city_id")
  private City fromCity;

  @ManyToOne
  @JoinColumn(name = "to_city_id")
  private City toCity;

  @Column(name = "distance")
  private Double distance;

  @Column(name = "speeed")
  private Double speed;

  private boolean connected;

  public Route(City fromCity, City toCity, Double distance, Double speed) {
    this.fromCity = fromCity;
    this.toCity = toCity;
    this.distance = distance;
    this.speed = speed;
    this.connected = true;
  }
}