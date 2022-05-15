package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.Route;
import com.pucp.odiparpackback.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
  
  private final RouteRepository routeRepository;

  public RouteService(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }
  
  public List<Route> findAll() {
    return routeRepository.findAll();
  }
  
  public Route save(Route route) {
    return routeRepository.save(route);
  }
  
  public Route update(Route route) {
    if(route.getId() == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id de la ruta no puede ser nulo");
    }
    Route routeDB = routeRepository.findById(route.getId()).orElse(null);
    if(routeDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ruta con id " + route.getId() + " no existe");
    }
    return routeRepository.save(route);
  }
  
  public Route delete(Long id) {
    Route route = routeRepository.findById(id).orElse(null);
    if(route == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ruta con id " + id + " no existe");
    }
    routeRepository.delete(route);
    return route;
  }
}
