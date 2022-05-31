package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.dto.CityDto;
import com.pucp.odiparpackback.dto.DepotDto;
import com.pucp.odiparpackback.dto.ProductOrderDto;
import com.pucp.odiparpackback.dto.TruckDto;
import com.pucp.odiparpackback.enums.Region;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Depot;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.CityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

  public ProductOrderDto productOrderToDto(ProductOrder source) {
    ProductOrderDto productOrderDto = map(source, ProductOrderDto.class);
    productOrderDto.setState(source.getState());
    productOrderDto.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrderDto.setRegistryDate(source.getRegistryDate());
    return productOrderDto;
  }

  public ProductOrder dtoToProductOrder(ProductOrderDto source) {
    ProductOrder productOrder = map(source, ProductOrder.class);
    productOrder.setState(source.getDeliveryState());
    productOrder.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrder.setRegistryDate(source.getRegistryDate());
    return productOrder;
  }

  public TruckDto truckToDto(Truck source) {

    TruckDto truckDto = map(source, TruckDto.class);
    truckDto.setUbigeo(source.getCurrentCity().getUbigeo());
    return truckDto;
  }

  public Truck dtoToTruck(TruckDto source) {
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

  public DepotDto depotToDto(Depot depot) {
    DepotDto depotDto = map(depot, DepotDto.class);
    depotDto.setCityUbigeo(depot.getCity().getUbigeo());
    return depotDto;
  }

  public Depot dtoToDepot(DepotDto source) {
    Depot depot = map(source, Depot.class);
    City city = cityRepository.findByUbigeo(source.getCityUbigeo()).get(0);
    if (city == null) {
      throw new IllegalArgumentException("No se encontro la ciudad con ubigeo: " + source.getCityUbigeo());
    }
    depot.setCity(city);
    return depot;
  }

  public CityDto cityToDto(City source) {
    CityDto cityDto = map(source, CityDto.class);
    cityDto.setRegion(source.getRegion().name());
    return cityDto;
  }

  public City dtoToCity(CityDto source) {
    City city = map(source, City.class);
    city.setRegion(Region.valueOf(source.getRegion()));
    return city;
  }
}
