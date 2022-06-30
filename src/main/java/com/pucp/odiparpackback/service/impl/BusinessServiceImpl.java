package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.OrderAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.TruckAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.DepotAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.NodeAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.TruckAlgorithmResponse;
import com.pucp.odiparpackback.model.*;
import com.pucp.odiparpackback.repository.*;
import com.pucp.odiparpackback.service.BusinessService;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
  private final ProductOrderRepository productOrderRepository;
  private final TransportationPlanRepository planRepository;
  private final TruckRepository truckRepository;
  private final AlgorithmService algorithmService;
  private final RouteRepository routeRepository;
  private final CityRepository cityRepository;

  @Override
  public void run() {
    while (true) {
      System.out.println("Starting process");
      List<ProductOrder> orderList = getProcessingOrders();
      List<Truck> truckList = getAvailableTrucks();

      updateStatus(orderList, truckList);
      algorithmCall(orderList, truckList);
      try {
        System.out.println("Thread sleeps");
        Thread.sleep(1000 * 60 * 5);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void algorithmCall(List<ProductOrder> orderList, List<Truck> truckList) {
    AlgorithmRequest request = constructAlgorithmRequest(orderList, truckList);
    AlgorithmResponse response = algorithmService.getPath(request);
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    for (DepotAlgorithmResponse d : response.getDepotList()) {
      for (TruckAlgorithmResponse t : d.getTruckList()) {
        List<TransportationPlan> transportationPlanList = new ArrayList<>();
        List<NodeAlgorithmResponse> nodeList = t.getNodeRoute();
//        NodeAlgorithmResponse n : t.getNodeRoute()
        for (int i = 0 ; i < nodeList.size() ; i++) {
          calendar.setTime(currentDate);
          //convert hour to ms
          calendar.add(Calendar.MILLISECOND, (int) (nodeList.get(i).getTravelCost() * 60 * 60 * 1000));
          ProductOrder po = null;
          if (nodeList.get(i).getIdOrder() > 0) {
            po = ProductOrder.builder().id(nodeList.get(i).getIdOrder()).build();
          }

          City city = cityRepository.findByUbigeo(nodeList.get(i).getUbigeo());
          Double speed = null;
          if (i < nodeList.size() - 1) {
            Route route = routeRepository.findRouteByFromCity_UbigeoAndToCity_Ubigeo(nodeList.get(i).getUbigeo(), nodeList.get(i + 1).getUbigeo());
            speed = route.getSpeed();
          }
          TransportationPlan transportationPlan = TransportationPlan.builder()
            .order(po)
            .routeStart(currentDate)
            .routeFinish(calendar.getTime())
            .cityFinish(city)
            .speed(speed)
            .build();
          if (calendar.getTimeInMillis() != currentDate.getTime()) {
            calendar.add(Calendar.HOUR, 1);
          }
          currentDate = calendar.getTime();
          transportationPlanList.add(transportationPlan);
        }

        Truck truck = truckList.stream().filter(tObject -> tObject.getId().equals(t.getId())).findFirst().orElse(null);
        if (Objects.isNull(truck)) {
          System.out.println("This shouldn't happen");
          return;
        }

        if (!transportationPlanList.isEmpty()) {
          transportationPlanList = planRepository.saveAll(transportationPlanList);
          truck.setStatus(TruckStatus.ONROUTE);
        }
        truck.setTransportationPlanList(transportationPlanList);
        System.out.println(truck);
      }
    }

    truckRepository.saveAll(truckList);
    orderList.forEach(productOrder -> productOrder.setState(OrderState.PENDING));
    productOrderRepository.saveAll(orderList);
  }

  private List<ProductOrder> getProcessingOrders() {
    List<ProductOrder> orderList = new ArrayList<>();
    try {
      orderList = productOrderRepository.findAllByState(OrderState.PROCESSING);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return orderList;
  }

  private List<Truck> getAvailableTrucks() {
    List<Truck> truckList = new ArrayList<>();
    try {
      truckList = truckRepository.findAllByStatusLessThanEqual(TruckStatus.AVAILABLE);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return truckList;
  }

  private void updateStatus(List<ProductOrder> orderList, List<Truck> truckList) {
    List<ProductOrder> deliveredOrderList = new ArrayList<>();
    List<Truck> updatedTruckList = new ArrayList<>();

    for (Truck t : truckList) {
      boolean updated = false;
      Date currentDate = new Date();
      City lastCity = t.getCurrentCity();
      for (TransportationPlan tPlan : t.getTransportationPlanList()) {
        ProductOrder po = orderList.stream().filter(o -> tPlan.getOrder().equals(o)).findFirst().orElse(null);
        if (Objects.isNull(po)) {
          System.out.println("Order not found");
          return;
        }

        if (tPlan.getRouteFinish().before(currentDate)) {
          po.setState(OrderState.DELIVERED);
          deliveredOrderList.add(po);
        } else {
          updated = true;
          t.setCurrentCity(lastCity);
        }
        lastCity = tPlan.getOrder().getDestination();
      }

      if (isDepot(t.getCurrentCity().getName()) && !t.getStatus().equals(TruckStatus.AVAILABLE)) {
        updated = true;
        t.setStatus(TruckStatus.AVAILABLE);
      }

      if (updated) {
        updatedTruckList.add(t);
      }
    }

    if (!deliveredOrderList.isEmpty()) {
      productOrderRepository.saveAll(deliveredOrderList);
    }

    if (!updatedTruckList.isEmpty()) {
      truckRepository.saveAll(updatedTruckList);
    }
  }

  private boolean isDepot(String name) {
    return name.equals("AREQUIPA") || name.equals("TRUJILLO") || name.equals("LIMA");
  }

  private AlgorithmRequest constructAlgorithmRequest(List<ProductOrder> orderList, List<Truck> truckList) {
    List<OrderAlgorithmRequest> orderAlgorithmList = new ArrayList<>();
    for (ProductOrder po : orderList) {
      double remainingTime = (double) (po.getMaxDeliveryDate().getTime() - po.getRegistryDate().getTime());
      remainingTime /= (1000 * 3600);
      OrderAlgorithmRequest orderAlgorithmRequest = OrderAlgorithmRequest.builder()
        .id(po.getId())
        .packages(po.getAmount())
        .ubigeo(po.getDestination().getUbigeo())
        .remainingTime(remainingTime)
        .build();
      orderAlgorithmList.add(orderAlgorithmRequest);
    }

    List<TruckAlgorithmRequest> truckAlgorithmList = new ArrayList<>();
    for (Truck t : truckList) {
      TruckAlgorithmRequest truckAlgorithmRequest = TruckAlgorithmRequest.builder()
        .id(t.getId())
        .ubigeo(t.getCurrentCity().getUbigeo())
        .maxLoad(t.getCapacity())
        .build();
      truckAlgorithmList.add(truckAlgorithmRequest);
    }

    return AlgorithmRequest.builder()
      .orderList(orderAlgorithmList)
      .truckList(truckAlgorithmList)
      .build();
  }
}
