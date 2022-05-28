package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.dto.CityDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

  @Autowired
  private CityRepository cityRepository;


  public List<CityDto> findAll() {
    return cityRepository.findAll().stream()
      .map(ObjectMapper::cityToDto)
      .collect(java.util.stream.Collectors.toList());
  }

  public City save(CityDto city) {
    return cityRepository.save(ObjectMapper.dtoToCity(city));
  }

  public City update(City city) {
    if (city.getId() == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id de la ciudad no puede ser nulo");
    }
    City cityDB = cityRepository.findById(city.getId()).orElse(null);
    if (cityDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ciudad con id " + city.getId() + " no existe");
    }
    return cityRepository.save(city);
  }

  public City delete(Long id) {
    City city = cityRepository.findById(id).orElse(null);
    if (city == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "La ciudad con id " + id + " no existe");
    }
    cityRepository.delete(city);
    return city;
  }

  public City findByUbigeo(String ubigeo) {
    return cityRepository.findByUbigeo(ubigeo);
  }

}
