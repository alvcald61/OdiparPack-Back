package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.model.Client;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.request.CityRequest;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.request.DepotRequest;
import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Depot;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.response.CityResponse;
import com.pucp.odiparpackback.response.ProductOrderResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ObjectMapper {

  private static final ModelMapper modelMapper = new ModelMapper();
  private static final Logger log = LogManager.getLogger(ObjectMapper.class);
  private final CityRepository cityRepository;

  public ObjectMapper(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  private <T, U> T map(U source, Class<T> destinationClass) {
    return modelMapper.map(source, destinationClass);
  }


  public TruckRequest truckToDto(Truck source) {

    TruckRequest truckDto = map(source, TruckRequest.class);
    truckDto.setUbigeo(source.getDepotUbigeo());
    return truckDto;
  }

  public Truck dtoToTruck(TruckRequest source) {
    try {
      Truck truck = map(source, Truck.class);
      System.out.println(cityRepository);
      City city = cityRepository.findByUbigeoCuston(source.getUbigeo()).get(0);
      if (city == null) {
        throw new IllegalArgumentException("No se encontro la ciudad con ubigeo: " + source.getUbigeo());
      }
      truck.setCurrentCity(city);
      return truck;
    } catch (IllegalArgumentException e) {
      log.error(source.getUbigeo() + " no es un ubigeo valido");
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public DepotRequest depotToDto(Depot depot) {
    DepotRequest depotDto = map(depot, DepotRequest.class);
    depotDto.setCityUbigeo(depot.getCity().getUbigeo());
    return depotDto;
  }

  public Depot dtoToDepot(DepotRequest source) {
    Depot depot = map(source, Depot.class);
    City city = cityRepository.findByUbigeo(source.getCityUbigeo());
    if (city == null) {
      throw new IllegalArgumentException("No se encontro la ciudad con ubigeo: " + source.getCityUbigeo());
    }
    depot.setCity(city);
    return depot;
  }

  public CityRequest cityToDto(City source) {
    CityRequest cityDto = map(source, CityRequest.class);
    Integer count = cityRepository.getDeliveryCount(source.getUbigeo());
    cityDto.setRegion(source.getRegion().name());
    log.trace("Count of city {}: {}", source.getUbigeo(), count);
    cityDto.setOrdersDeliveredCounter(count);
    return cityDto;
  }

  public City dtoToCity(CityRequest source) {
    City city = map(source, City.class);
    city.setRegion(Region.valueOf(source.getRegion()));
    return city;
  }

  public CityResponse mapCity(City city) {
    return CityResponse.builder()
            .id(city.getId())
            .name(city.getName())
            .ubigeo(city.getUbigeo())
            .longitude(city.getLongitude())
            .latitude(city.getLatitude())
            .region(city.getRegion().name())
            .build();
  }

  public ClientResponse mapClient(Client client) {
    if (Objects.nonNull(client)) {
      return ClientResponse.builder()
              .id(client.getId())
              .name(client.getName())
              .ruc(client.getRuc())
              .build();
    }

    return null;
  }
}
