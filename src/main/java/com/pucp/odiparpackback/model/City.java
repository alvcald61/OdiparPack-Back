package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.utils.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  private String ubigeo;

  @Enumerated
  @Column(name = "region", nullable = false)
  private Region region;

  @OneToMany(mappedBy = "city", orphanRemoval = true)
  private Set<Client> clients = new LinkedHashSet<>();

  @OneToMany(mappedBy = "fromCity", orphanRemoval = true)
  private Set<Route> fromRoutes = new LinkedHashSet<>();

  @Override
  public String toString() {
    return "City{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", longitude='" + longitude + '\'' +
      ", latitude='" + latitude + '\'' +
      ", ubigeo='" + ubigeo + '\'' +
      '}';
  }
}