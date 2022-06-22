package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.OrderAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.TruckAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.DepotAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.TruckAlgorithmResponse;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl {
  private final ProductOrderRepository productOrderRepository;
  private final TruckRepository truckRepository;
  private final AlgorithmService algorithmService;

  public void algorithmCall() {
    List<ProductOrder> orderList = getProcessingOrders();
    List<Truck> truckList = getTruckList();

    updateStatus(orderList, truckList);

    AlgorithmRequest request = constructAlgorithmRequest(orderList, truckList);
    AlgorithmResponse response = algorithmService.getPath(request);
//    for (DepotAlgorithmResponse d : response.getDepotList()) {
//      for (TruckAlgorithmResponse t : d.getTruckList()) {
//
//      }
//    }
  }

  private List<ProductOrder> getProcessingOrders() {
    List<ProductOrder> orderList;
    try {
      orderList = productOrderRepository.findAllByState(OrderState.PROCESSING);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return orderList;
  }

  private List<Truck> getTruckList() {
    List<Truck> truckList;
    try {
      truckList = truckRepository.findAllByStatusLessThanEqual(TruckStatus.ONROUTE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return truckList;
  }

  private void updateStatus(List<ProductOrder> orderList, List<Truck> truckList) {
    for (Truck t : truckList) {
      System.out.println(t.getTransportationPlanList());
    }
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
