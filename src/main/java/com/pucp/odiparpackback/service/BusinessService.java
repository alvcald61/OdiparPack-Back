package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.Truck;

import java.util.List;

public interface BusinessService {
  void run();
  void updateStatus(List<ProductOrder> orderList, List<Truck> truckList, List<Long> maintenanceTrucks);
}