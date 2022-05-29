package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.dto.*;
import com.pucp.odiparpackback.enums.Region;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Depot;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.service.CityService;
import org.modelmapper.ModelMapper;

public class ObjectMapper {

  private static final ModelMapper modelMapper = new ModelMapper();

  private static final CityService cityService = new CityService();


  private static <T, U> T map(U source, Class<T> destinationClass) {
    return modelMapper.map(source, destinationClass);
  }

  public static ProductOrderDto productOrderToDto(ProductOrder source) {
    ProductOrderDto productOrderDto = map(source, ProductOrderDto.class);
    productOrderDto.setState(source.getState());
    productOrderDto.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrderDto.setRegistryDate(source.getRegistryDate());
    return productOrderDto;
  }

  public static ProductOrder dtoToProductOrder(ProductOrderDto source) {
    ProductOrder productOrder = map(source, ProductOrder.class);
    productOrder.setState(source.getDeliveryState());
    productOrder.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrder.setRegistryDate(source.getRegistryDate());
    return productOrder;
  }

  public static TruckDto truckToDto(Truck source) {
    TruckDto truckDto = map(source, TruckDto.class);
    truckDto.setUbigeo(source.getCurrentCity().getUbigeo());
    return truckDto;
  }

  public static Truck dtoToTruck(TruckDto source) {
    Truck truck = map(source, Truck.class);
    City city = cityService.findByUbigeo(source.getUbigeo());
    if (city == null) {
      throw new IllegalArgumentException("No se encontro la ciudad con ubigeo: " + source.getUbigeo());
    }
    truck.setCurrentCity(city);
    return truck;
  }

  public static DepotDto depotToDto(Depot depot) {
    DepotDto depotDto = map(depot, DepotDto.class);
    depotDto.setCityUbigeo(depot.getCity().getUbigeo());
    return depotDto;
  }

  public static Depot dtoToDepot(DepotDto source) {
    Depot depot = map(source, Depot.class);
    City city = cityService.findByUbigeo(source.getCityUbigeo());
    if (city == null) {
      throw new IllegalArgumentException("No se encontro la ciudad con ubigeo: " + source.getCityUbigeo());
    }
    depot.setCity(city);
    return depot;
  }

  public static CityDto cityToDto(City source) {
    CityDto cityDto = map(source, CityDto.class);
    cityDto.setRegion(source.getRegion().name());
    source.getFromRoutes().forEach(route -> cityDto.getConnections().add(new RouteDto(route.getId(), route.getFromCity().getUbigeo(), route.getToCity().getUbigeo(), route.getDistance(), route.getSpeed())));
    return cityDto;
  }

  public static City dtoToCity(CityDto source) {
    City city = map(source, City.class);
    city.setRegion(Region.valueOf(source.getRegion()));
    return city;
  }
}
