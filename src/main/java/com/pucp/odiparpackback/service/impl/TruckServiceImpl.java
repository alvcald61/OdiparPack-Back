package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.response.CityResponse;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.ProductOrderResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.response.TransportationPlanResponse;
import com.pucp.odiparpackback.response.TruckResponse;
import com.pucp.odiparpackback.service.TruckService;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.ObjectMapper;
import com.pucp.odiparpackback.utils.TimeUtil;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {

  private static final Logger log = LogManager.getLogger(TruckServiceImpl.class);
  private final TruckRepository truckRepository;
  private final ObjectMapper objectMapper;

  @Override
  public StandardResponse<List<TruckRequest>> findAllSimulation() {
    try {
      List<TruckRequest> responseList = truckRepository.findAllByStatusLessThanEqual(TruckStatus.MAINTENANCE).stream().map(objectMapper::truckToDto).collect(Collectors.toList());
      return new StandardResponse<>(responseList);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(e.getMessage());
      return new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public StandardResponse<List<TruckResponse>> findAll() {
    try {
      List<Truck> list = truckRepository.findAllByStatusLessThanEqual(TruckStatus.MAINTENANCE);
      List<TruckResponse> responseList = new ArrayList<>();
      Date currentDate = new Date();

      for (Truck truck : list) {
        List<TransportationPlan> tpList = truck.getTransportationPlanList();
        List<TransportationPlanResponse> planList = new ArrayList<>();
        CityResponse currentCity = objectMapper.mapCity(truck.getCurrentCity());

        TruckResponse truckResponse = TruckResponse.builder()
                .truckId(truck.getId())
                .capacity(truck.getCapacity())
                .currentCity(currentCity)
                .plate(truck.getPlate())
                .status(truck.getStatus())
                .transportationPlanList(planList)
                .build();

        mapTruck(truckResponse, tpList, planList, currentDate);

        responseList.add(truckResponse);

      }
      return new StandardResponse<>(responseList);
    } catch (Exception e) {
      log.error("Error en el back", e);
      ErrorResponse error = new ErrorResponse(e.getMessage());
      return new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @Override
  public TruckRequest save(TruckRequest truckDto) {
    return objectMapper.truckToDto(truckRepository.save(objectMapper.dtoToTruck(truckDto)));
  }

  @Override
  public StandardResponse<TruckResponse> findOne(Long truckId) {
    Truck truck = truckRepository.findById(truckId).orElse(null);
    if (Objects.isNull(truck)) {
      ErrorResponse error = new ErrorResponse(Message.RESOURCE_NOT_FOUND);
      return new StandardResponse<>(error, HttpStatus.NOT_FOUND);
    }
    Date currentDate = new Date();

    List<TransportationPlan> tpList = truck.getTransportationPlanList();
    List<TransportationPlanResponse> planList = new ArrayList<>();
    CityResponse currentCity = objectMapper.mapCity(truck.getCurrentCity());

    TruckResponse truckResponse = TruckResponse.builder()
            .truckId(truckId)
            .plate(truck.getPlate())
            .capacity(truck.getCapacity())
            .currentCity(currentCity)
            .transportationPlanList(planList)
            .status(truck.getStatus())
            .build();

    mapTruck(truckResponse, tpList, planList, currentDate);

    return new StandardResponse<>(truckResponse);
  }

  private void mapTruck(TruckResponse truckResponse, List<TransportationPlan> tpList, List<TransportationPlanResponse> planList, Date currentDate) {
    if (!tpList.isEmpty()) {
      TransportationPlan previous = tpList.get(0);
      tpList.sort(((t1, t2) -> (int) (t1.getId() - t2.getId())));
      for (TransportationPlan plan : tpList) {
        int indexFound = tpList.indexOf(plan);
        if (plan.getRouteStart().before(plan.getRouteFinish()) && plan.getRouteFinish().after(currentDate)
                && previous.getRouteFinish().before(currentDate) && indexFound!=tpList.size()-1 ) {
          setTruckLocation(truckResponse, plan, tpList.get(indexFound+1), currentDate);
        }

        ProductOrder po = plan.getOrder();
        ProductOrderResponse productOrderResponse = createOrderResponse(po);
        CityResponse cityResponse = objectMapper.mapCity(plan.getCity());
        Double speed = null;
        if (Objects.nonNull(plan.getSpeed())) {
          speed = plan.getSpeed().getSpeed();
        }
        planList.add(TransportationPlanResponse.builder()
                .idTransportationPlan(plan.getId())
                .routeStart(TimeUtil.formatDate(plan.getRouteStart()))
                .routeFinish(TimeUtil.formatDate(plan.getRouteFinish()))
                .order(productOrderResponse)
                .speed(speed)
                .city(cityResponse)
                .build());
        previous = plan;
      }
    }
  }

  private ProductOrderResponse createOrderResponse(ProductOrder order) {
    if (Objects.isNull(order)) {
      return null;
    }

    ClientResponse clientResponse = objectMapper.mapClient(order.getClient());
    return ProductOrderResponse.builder()
            .id(order.getId())
            .maxDeliveryDate(TimeUtil.formatDate(order.getMaxDeliveryDate()))
            .registryDate(TimeUtil.formatDate(order.getRegistryDate()))
            .state(order.getState().name())
            .client(clientResponse)
            .amount(order.getAmount())
            .build();
  }

  private void setTruckLocation(TruckResponse truck, TransportationPlan previous, TransportationPlan plan, Date currentDate) {
    double traveledFraction = (double) (currentDate.getTime() - previous.getRouteStart().getTime()) /
            (double) (previous.getRouteFinish().getTime() - previous.getRouteStart().getTime());
    double longitude = previous.getCity().getLongitude();
    longitude += (plan.getCity().getLongitude() - longitude) * traveledFraction;
    double latitude = previous.getCity().getLatitude();
    latitude += (plan.getCity().getLatitude() - latitude) * traveledFraction;

    truck.setLatitude(latitude);
    truck.setLongitude(longitude);
    truck.setCurrentCity(objectMapper.mapCity(previous.getCity()));
  }
}
