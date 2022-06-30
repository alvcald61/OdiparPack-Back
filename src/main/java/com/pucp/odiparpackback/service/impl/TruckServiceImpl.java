package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.response.*;
import com.pucp.odiparpackback.service.TruckService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import com.pucp.odiparpackback.utils.TimeUtil;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
      for (Truck t : list) {
        List<TransportationPlan> tpList = t.getTransportationPlanList();
        tpList.sort(Comparator.comparing(TransportationPlan::getRouteFinish));
        List<TransportationPlanResponse> transportationPlanResponseList = new ArrayList<>();

        for (TransportationPlan tPlan : tpList) {
          ProductOrder po = tPlan.getOrder();
          ProductOrderResponse productOrderResponse = createOrderResponse(po);

          transportationPlanResponseList.add(TransportationPlanResponse.builder()
            .idTransportationPlan(tPlan.getId())
            .order(productOrderResponse)
            .routeStart(TimeUtil.formatDate(tPlan.getRouteStart()))
            .routeFinish(TimeUtil.formatDate(tPlan.getRouteFinish()))
            .cityFinish(objectMapper.mapCity(tPlan.getCityFinish()))
            .speed(tPlan.getSpeed())
            .build());
        }

        CityResponse currentCity = objectMapper.mapCity(t.getCurrentCity());
        TruckResponse truck = TruckResponse.builder()
          .truckId(t.getId())
          .capacity(t.getCapacity())
          .currentCity(currentCity)
          .plate(t.getPlate())
          .status(t.getStatus())
          .transportationPlanList(transportationPlanResponseList)
          .build();
        responseList.add(truck);
      }
      return new StandardResponse<>(responseList);
    } catch (Exception e) {
      log.error("Error en el back", e);
      ErrorResponse error = new ErrorResponse(e.getMessage());
      return new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  private ProductOrderResponse createOrderResponse(ProductOrder order) {
    if (Objects.isNull(order)) {
      return null;
    }

    CityResponse cityResponse = objectMapper.mapCity(order.getDestination());
    ClientResponse clientResponse = objectMapper.mapClient(order.getClient());
    return ProductOrderResponse.builder()
      .id(order.getId())
      .maxDeliveryDate(TimeUtil.formatDate(order.getMaxDeliveryDate()))
      .registryDate(TimeUtil.formatDate(order.getRegistryDate()))
      .state(order.getState().name())
      .destination(cityResponse)
      .client(clientResponse)
      .amount(order.getAmount())
      .build();
  }


  @Override
  public TruckRequest save(TruckRequest truckDto) {
    return objectMapper.truckToDto(truckRepository.save(objectMapper.dtoToTruck(truckDto)));
  }
}
