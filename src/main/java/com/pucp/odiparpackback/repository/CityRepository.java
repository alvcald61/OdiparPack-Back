package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  City findByUbigeo(String ubigeo);

  List<City> findByName(String name);

  @Query(value = "SELECT c FROM City c WHERE c.ubigeo= ?1")
  List<City> findByUbigeoCuston(String ubigeo);

  @Query("SELECT c FROM City c WHERE c.ubigeo in ?1")
  List<City> findAllByUbigeoList(List<String> ubigeoList);

  @Query("select sum(h.packages) from Historic h where h.orderDate > '2022-07-18 18:01:00.000' and h.orderDate < '2022-10-18 00:00:00.000' and h.destinationNode = ?1 group by h.destinationNode ")
  Integer getDeliveryCount(String ubigeo);
}
