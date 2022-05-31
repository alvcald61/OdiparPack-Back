package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  List<City> findByUbigeo(String ubigeo);

  List<City> findByName(String name);

  @Query(value = "SELECT c FROM City c WHERE c.ubigeo= ?1")
  List<City> findByUbigeoCuston(String ubigeo);

}
