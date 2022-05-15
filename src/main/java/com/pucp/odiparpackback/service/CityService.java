package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
  private final CityRepository cityRepository;

  public CityService(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  public List<City> findAll() {
    return cityRepository.findAll();
  }
  
  public City save(City city) {
    return cityRepository.save(city);
  }
  
  public City update(City city) {
    if(city.getId() == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id de la ciudad no puede ser nulo");
    }
    City cityDB = cityRepository.findById(city.getId()).orElse(null);
    if(cityDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ciudad con id " + city.getId() + " no existe");
    }
    return cityRepository.save(city);
  }
  
  public City delete(Long id) {
    City city = cityRepository.findById(id).orElse(null);
    if(city == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ciudad con id " + id + " no existe");
    }
    cityRepository.delete(city);
    return city;
  }
  
}
