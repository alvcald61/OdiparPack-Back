package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
  Route findRouteByFromCity_UbigeoAndToCity_Ubigeo(String fromCity, String toCity);
}
