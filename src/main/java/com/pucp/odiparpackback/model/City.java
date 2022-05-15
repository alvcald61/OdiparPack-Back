package com.pucp.odiparpackback.model;

import com.pucp.odiparpackback.enums.Region;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;  

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "longitude", nullable = false)
  private String longitude;

  @Column(name = "latitude", nullable = false)
  private String latitude;

  @Enumerated
  @Column(name = "region", nullable = false)
  private Region region;

  @OneToMany(mappedBy = "city", orphanRemoval = true)
  private Set<Client> clients = new LinkedHashSet<>();

  @OneToMany(mappedBy = "fromCity", orphanRemoval = true)
  private Set<Route> fromRoutes = new LinkedHashSet<>();
}