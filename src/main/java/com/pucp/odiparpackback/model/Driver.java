package com.pucp.odiparpackback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Driver {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "names")
  private String names;

  @Column(name = "last_names")
  private String lastNames;

  @Column(name = "dni", length = 8)
  private String dni;

  @Column(name = "license")
  private String license;

  @OneToOne(mappedBy = "driver")
  private Truck truck;

}