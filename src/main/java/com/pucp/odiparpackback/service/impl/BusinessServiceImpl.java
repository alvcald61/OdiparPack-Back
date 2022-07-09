package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.OrderAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.TruckAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.DepotAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.NodeAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.SubOrderResponse;
import com.pucp.odiparpackback.algorithm.response.TruckAlgorithmResponse;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.repository.TransportationPlanRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.service.BusinessService;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.Speed;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
  private final ProductOrderRepository productOrderRepository;
  private final TransportationPlanRepository planRepository;
  private final TruckRepository truckRepository;
  private final CityRepository cityRepository;
  private final AlgorithmService algorithmService;

  @Override
  public void run() {
    System.out.println("Starting process");
    List<ProductOrder> orderList = getProcessingOrders();
    List<Truck> truckList = getAvailableTrucks();

    updateStatus(orderList, truckList);
    algorithmCall(orderList, truckList);
  }

  private void algorithmCall(List<ProductOrder> orderList, List<Truck> truckList) {
    AlgorithmRequest request = constructAlgorithmRequest(orderList, truckList);
    AlgorithmResponse response = algorithmService.getPath(request);

    Calendar calendar = Calendar.getInstance();
    for (DepotAlgorithmResponse d : response.getDepotList()) {
      for (TruckAlgorithmResponse t : d.getTruckList()) {
        Date currentDate = new Date();
        List<TransportationPlan> transportationPlanList = new ArrayList<>();

        City previousCity = null;
        for (NodeAlgorithmResponse n : t.getNodeRoute()) {
          calendar.setTime(currentDate);
          //convert hour to ms
          calendar.add(Calendar.MILLISECOND, (int) (n.getTravelCost() * 60 * 60 * 1000));
          ProductOrder po = null;
          if (n.getIdOrder() > 0) {
            po = ProductOrder.builder().id(n.getIdOrder()).build();
          }

          City city = cityRepository.findByUbigeo(n.getUbigeo());
          Speed speed = null;
          if (Objects.nonNull(previousCity)) {
            speed = Speed.valueOf(city.getRegion().name() + previousCity.getRegion().name());
          }

          SubOrderResponse subOrder = null;
          if (n.getIdOrder() > 0) {
            subOrder = t.getOrderList().stream().filter(s -> s.getOrderId().equals(n.getIdOrder())).findFirst().orElse(null);
            t.getOrderList().remove(subOrder);
          }
          TransportationPlan transportationPlan = TransportationPlan.builder()
                  .order(po)
                  .city(city)
                  .routeStart(currentDate)
                  .routeFinish(calendar.getTime())
                  .speed(speed)
                  .amount(Objects.isNull(subOrder) ? null : subOrder.getPackageAmount())
                  .build();
          if (calendar.getTimeInMillis() != currentDate.getTime()) {
            calendar.add(Calendar.HOUR, 1);
          }
          currentDate = calendar.getTime();
          transportationPlanList.add(transportationPlan);
          previousCity = city;
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
      truckList = truckRepository.findAllByStatus(TruckStatus.AVAILABLE);
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
      //cambiar por fecha actual en vez de fecha de registro
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
