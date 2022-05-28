package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  City findByUbigeo(String ubigeo);
}
