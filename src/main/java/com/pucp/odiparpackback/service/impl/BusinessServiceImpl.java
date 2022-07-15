package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.BlockadeAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.OrderAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.TruckAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.DepotAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.NodeAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.SubOrderResponse;
import com.pucp.odiparpackback.algorithm.response.TruckAlgorithmResponse;
import com.pucp.odiparpackback.model.Breakdown;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.RouteBlock;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.repository.RouteBlockRepository;
import com.pucp.odiparpackback.repository.TransportationPlanRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.BusinessService;
import com.pucp.odiparpackback.service.MaintenanceService;
import com.pucp.odiparpackback.utils.BreakdownType;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.Speed;
import com.pucp.odiparpackback.utils.TimeUtil;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
  private final ProductOrderRepository productOrderRepository;
  private final TransportationPlanRepository planRepository;
  private final TruckRepository truckRepository;
  private final CityRepository cityRepository;
  private final RouteBlockRepository routeBlockRepository;
  private final AlgorithmService algorithmService;
  private final MaintenanceService maintenanceService;

  @Override
  public void run() {
    System.out.println("Starting process");
    List<ProductOrder> orderList = getOrders();
    List<Truck> truckList = getTrucks();
    List<Long> maintenanceTrucks = getMaintenanceTrucks();

    updateStatus(orderList, truckList, maintenanceTrucks);
    //TODO: agregar condicion de replanificacion
    if (orderList.stream().anyMatch(o -> o.getState().equals(OrderState.PROCESSING)))
      algorithmCall(orderList, truckList, maintenanceTrucks);
  }

  private void algorithmCall(List<ProductOrder> orderList, List<Truck> truckList, List<Long> maintenanceTrucks) {
    List<RouteBlock> blockList = getCurrentBlockades();
    List<City> cityList = getCityList();
    AlgorithmRequest request = constructAlgorithmRequest(orderList, truckList, blockList, maintenanceTrucks);
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

          City city = cityList.stream().filter(c -> c.getUbigeo().equals(n.getUbigeo())).findFirst().orElse(null);
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
      }
    }

    truckRepository.saveAll(truckList);
    List<ProductOrder> filteredList = orderList.stream().filter(p -> p.getState().equals(OrderState.PROCESSING)).collect(Collectors.toList());
    filteredList.forEach(productOrder -> productOrder.setState(OrderState.PENDING));
    productOrderRepository.saveAll(filteredList);
  }

  @Override
  public void updateStatus(List<ProductOrder> orderList, List<Truck> truckList, List<Long> maintenanceTrucks) {
    List<ProductOrder> deliveredOrderList = new ArrayList<>();
    List<Truck> updatedTruckList = new ArrayList<>();

    for (Truck t : truckList) {
      boolean updated = false;
      Date currentDate = new Date();
      Calendar calendar = Calendar.getInstance();
      List<TransportationPlan> planList = t.getTransportationPlanList();

      if (Objects.nonNull(maintenanceTrucks.stream().filter(id -> t.getId().equals(id)).findFirst().orElse(null))) {
        calendar.setTime(t.getMaintenance().getInitialDate());
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        if (t.getMaintenance().getInitialDate().before(currentDate) && calendar.getTime().after(currentDate)) {
          if (!planList.isEmpty()) {
            System.out.println("Truck is carrying load, it should return to closest depot");
          }
          t.setStatus(TruckStatus.MAINTENANCE);
          updated = true;
        }
      }

      switch (t.getStatus()) {
        case MAINTENANCE:
          calendar.setTime(t.getMaintenance().getInitialDate());
          calendar.add(Calendar.DAY_OF_WEEK, 1);
          if (calendar.getTime().before(currentDate)) {
            t.setStatus(TruckStatus.AVAILABLE);
            updated = true;
          }
          break;
        case ONROUTE:
          City lastCity = t.getCurrentCity();
          planList.sort(((t1, t2) -> (int) (t1.getId() - t2.getId())));
          TransportationPlan previous = planList.get(0);
          for (TransportationPlan tPlan : planList) {
            ProductOrder po = orderList.stream().filter(o -> o.equals(tPlan.getOrder())).findFirst().orElse(null);

            if (tPlan.getRouteFinish().after(currentDate) && previous.getRouteFinish().before(currentDate)) {
              t.setCurrentCity(previous.getCity());
            } else if (Objects.nonNull(po) && !po.getState().equals(OrderState.DELIVERED) && tPlan.getRouteFinish().before(currentDate)) {
              po.setState(OrderState.DELIVERED);
              deliveredOrderList.add(po);
            }
            previous = tPlan;
          }

          if (previous.getRouteFinish().before(currentDate)) {
            updated = true;
            t.setCurrentCity(previous.getCity());
            t.setStatus(TruckStatus.AVAILABLE);
          } else if (!lastCity.equals(t.getCurrentCity())) {
            updated = true;
          }
          break;
        case BROKEDOWN:
          Breakdown breakdown = t.getBreakdown();
          if (!breakdown.getBreakdownType().equals(BreakdownType.SINIESTRO) && breakdown.getEndDate().before(currentDate)) {
            updated = true;
            t.setStatus(breakdown.getBreakdownType().equals(BreakdownType.MODERADA) ? TruckStatus.ONROUTE : TruckStatus.AVAILABLE);
            if (breakdown.getBreakdownType().equals(BreakdownType.FUERTE)) {
              City depot = cityRepository.findByUbigeo(t.getDepotUbigeo());
              t.setCurrentCity(depot);
            }
          }
          break;
        default:
          break;
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

  private AlgorithmRequest constructAlgorithmRequest(List<ProductOrder> orderList, List<Truck> truckList, List<RouteBlock> blockList, List<Long> maintenanceTrucks) {
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
    List<Truck> filteredList = truckList.stream().filter(truck -> truck.getStatus().equals(TruckStatus.AVAILABLE)).collect(Collectors.toList());
    if (filteredList.isEmpty() || excessCapacity(filteredList, orderList)) {
      filteredList.addAll(truckList.stream().filter(truck -> truck.getStatus().equals(TruckStatus.ONROUTE)).collect(Collectors.toList()));
    }
    for (Truck t : filteredList) {
      if (Objects.nonNull(maintenanceTrucks.stream().filter(id -> t.getId().equals(id)).findFirst().orElse(null))) {
        continue;
      }

      TruckAlgorithmRequest truckAlgorithmRequest = TruckAlgorithmRequest.builder()
              .id(t.getId())
              .ubigeo(t.getCurrentCity().getUbigeo())
              .maxLoad(t.getStatus().equals(TruckStatus.ONROUTE) ? getTruckCapacity(t)  : t.getCapacity())
              .build();
      truckAlgorithmList.add(truckAlgorithmRequest);
    }

    List<BlockadeAlgorithmRequest> blockadeAlgorithmList = new ArrayList<>();
    Date currentDate = new Date();
    for (RouteBlock r : blockList) {
      double start = (r.getStartDate().getTime() - currentDate.getTime()) / 1000.0;
      double end = (r.getEndDate().getTime() - currentDate.getTime()) / 1000.0;
      BlockadeAlgorithmRequest blockade = BlockadeAlgorithmRequest.builder()
              .firstNode(r.getStartCity().getUbigeo())
              .secondNode(r.getEndCity().getUbigeo())
              .start(start / 3600)
              .end(end / 3600)
              .build();

      blockadeAlgorithmList.add(blockade);
    }

    return AlgorithmRequest.builder()
            .orderList(orderAlgorithmList)
            .truckList(truckAlgorithmList)
            .blockadeList(blockadeAlgorithmList)
            .build();
  }

  private boolean excessCapacity(List<Truck> filteredList, List<ProductOrder> orderList) {
    AtomicInteger totalCapacity = new AtomicInteger();
    AtomicInteger totalPackages = new AtomicInteger();
    filteredList.forEach(truck -> totalCapacity.addAndGet(truck.getCapacity()));
    orderList.forEach(order -> totalCapacity.addAndGet(order.getAmount()));
    return totalCapacity.get() < totalPackages.get();
  }

  private Integer getTruckCapacity(Truck t) {
    AtomicInteger load = new AtomicInteger();
    t.getTransportationPlanList()
            .forEach(plan -> load.addAndGet(Objects.nonNull(plan.getAmount()) ? plan.getAmount() : 0));
    return load.get();
  }

  private List<City> getCityList() {
    return cityRepository.findAll();
  }

  private List<RouteBlock> getCurrentBlockades() {
    List<RouteBlock> blockList = new ArrayList<>();
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);
    calendar.add(Calendar.HOUR, 72);
    Date finalDate = calendar.getTime();
    try {
      blockList = routeBlockRepository.findAllBetween(currentDate, finalDate);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return blockList;
  }

  private List<ProductOrder> getOrders() {
    List<ProductOrder> orderList = new ArrayList<>();
    try {
      orderList = productOrderRepository.findAllByStateLessThanEqual(OrderState.PROCESSING);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return orderList;
  }

  private List<Truck> getTrucks() {
    List<Truck> truckList = new ArrayList<>();
    try {
      truckList = truckRepository.findAll();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return truckList;
  }

  private List<Long> getMaintenanceTrucks() {
    Date currentDate = new Date();
    StandardResponse<List<Long>> response = maintenanceService.listByDate(TimeUtil.formatDate(currentDate));
    if (response.getStatus().equals(HttpStatus.OK)) {
      return response.getData();
    }
    System.out.println("Error al obtener mantenimientos");
    return new ArrayList<>();
  }
}
