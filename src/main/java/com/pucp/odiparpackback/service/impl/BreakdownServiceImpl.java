package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.BlockadeAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.OrderAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.request.TruckAlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.DepotAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.NodeAlgorithmResponse;
import com.pucp.odiparpackback.algorithm.response.TruckAlgorithmResponse;
import com.pucp.odiparpackback.model.Breakdown;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.RouteBlock;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.BreakdownRepository;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.repository.RouteBlockRepository;
import com.pucp.odiparpackback.repository.TransportationPlanRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.request.BreakdownRequest;
import com.pucp.odiparpackback.response.BreakdownResponse;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.BreakdownService;
import com.pucp.odiparpackback.service.BusinessService;
import com.pucp.odiparpackback.service.MaintenanceService;
import com.pucp.odiparpackback.utils.BreakdownType;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.Speed;
import com.pucp.odiparpackback.utils.TimeUtil;
import com.pucp.odiparpackback.utils.TruckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreakdownServiceImpl implements BreakdownService {
  private final BreakdownRepository breakdownRepository;
  private final TruckRepository truckRepository;
  private final TransportationPlanRepository transportationPlanRepository;
  private final ProductOrderRepository productOrderRepository;
  private final RouteBlockRepository routeBlockRepository;
  private final CityRepository cityRepository;
  private final BusinessService businessService;
  private final AlgorithmService algorithmService;
  private final MaintenanceService maintenanceService;

  @Override
  public StandardResponse<BreakdownResponse> getTruckBreakdown(Truck truck) {
    return null;
  }

  @Override
  public StandardResponse<BreakdownResponse> createBreakdown(BreakdownRequest request) {
    StandardResponse<BreakdownResponse> response;
    try {
      Truck truck = truckRepository.getById(request.getTruckId());

      if (truck.getStatus().equals(TruckStatus.AVAILABLE)) {
        ErrorResponse error = new ErrorResponse(Message.AVAILABLE_ERROR);
        response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
        return response;
      } else if (truck.getStatus().equals(TruckStatus.BROKEDOWN)) {
        ErrorResponse error = new ErrorResponse(Message.BREAKDOWN_ERROR);
        response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
        return response;
      }

      Breakdown breakdown = truck.getBreakdown();
      breakdown.setBreakdownType(BreakdownType.valueOf(request.getBreakdownType()));
      replan(truck, breakdown);

      BreakdownResponse breakdownResponse = BreakdownResponse.builder()
              .breakdownId(breakdown.getId())
              .type(breakdown.getBreakdownType())
              .build();
      response = new StandardResponse<>(breakdownResponse);
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  private void replan(Truck truck, Breakdown breakdown) {
    Date currentDate = new Date();
    List<Truck> truckList = getTrucks();
    List<ProductOrder> orderList = getOrders();
    List<Long> maintenanceTrucks = getMaintenanceTrucks();

    breakdown.setStartDate(currentDate);
    businessService.updateStatus(orderList, truckList, maintenanceTrucks);
    truck.setStatus(TruckStatus.BROKEDOWN);
    switch (breakdown.getBreakdownType()) {
      case MODERADA:
        simpleBreakdown(truck, breakdown);
        break;
      case FUERTE:
        strongBreakdown(truck, breakdown, truckList);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR, 72);
        breakdown.setEndDate(calendar.getTime());
        break;
      case SINIESTRO:
        strongBreakdown(truck, breakdown, truckList);
        break;
      default:
        break;
    }
    breakdownRepository.save(breakdown);
  }

  private void simpleBreakdown(Truck truck, Breakdown breakdown) {
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    List<TransportationPlan> transportationPlanList = truck.getTransportationPlanList();
    TransportationPlan previous = transportationPlanList.get(0);
    for (TransportationPlan t : transportationPlanList) {
      if (t.getRouteFinish().after(currentDate)) {
        previous = t;
        continue;
      } else if (!t.getRouteStart().after(currentDate)) {
        setTruckLocation(breakdown, previous, t, currentDate);
      }
      Date start = t.getRouteStart();
      Date end = t.getRouteFinish();

      calendar.setTime(start);
      calendar.add(Calendar.HOUR, 12);
      t.setRouteStart(calendar.getTime());
      calendar.setTime(end);
      calendar.add(Calendar.HOUR, 12);
      t.setRouteFinish(calendar.getTime());
    }

    calendar.setTime(currentDate);
    calendar.add(Calendar.HOUR, 12);
    breakdown.setEndDate(calendar.getTime());

    truck.setStatus(TruckStatus.BROKEDOWN);
    truck.setBreakdown(breakdown);

    transportationPlanRepository.saveAll(transportationPlanList);
    truckRepository.save(truck);
  }

  private void strongBreakdown(Truck truck, Breakdown breakdown, List<Truck> truckList) {
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    List<TransportationPlan> transportationPlanList = truck.getTransportationPlanList();
    List<RouteBlock> blockList = getCurrentBlockades();
    List<City> cityList = getCityList();
    transportationPlanList.sort(((t1, t2) -> (int) (t1.getId() - t2.getId())));

    TransportationPlan previous = transportationPlanList.get(0);
    List<TransportationPlan> remainingList = new ArrayList<>();
    int packageAmount = 0;
    for (TransportationPlan plan : transportationPlanList) {
      if (plan.getRouteFinish().after(currentDate)) {
        packageAmount += plan.getAmount();
        remainingList.add(plan);
      } else {
        previous = plan;
      }
    }

    Truck newTruck = getNewTruck(truckList, packageAmount);
    AlgorithmRequest request = constructAlgorithmRequest(previous, newTruck, packageAmount, blockList);
    AlgorithmResponse response = algorithmService.getPath(request);
    DepotAlgorithmResponse depot = response.getDepotList().stream().filter(d -> !d.getTruckList().isEmpty()).findFirst().orElse(null);
    TruckAlgorithmResponse truckResponse = depot.getTruckList().get(0);

    List<TransportationPlan> newTransportationPlan = new ArrayList<>();
    City previousCity = null;
    int totalMs = 0;
    for (NodeAlgorithmResponse n : truckResponse.getNodeRoute()) {
      calendar.setTime(currentDate);
      totalMs += (int) (n.getTravelCost() * 60 * 60 * 1000);
      calendar.add(Calendar.MILLISECOND, (int) (n.getTravelCost() * 60 * 60 * 1000));

      City city = cityList.stream().filter(c -> c.getUbigeo().equals(n.getUbigeo())).findFirst().orElse(null);
      Speed speed = null;
      if (Objects.nonNull(previousCity)) {
        speed = Speed.valueOf(city.getRegion().name() + previousCity.getRegion().name());
      }

      TransportationPlan transportationPlan = TransportationPlan.builder()
              .city(city)
              .routeStart(currentDate)
              .routeFinish(calendar.getTime())
              .speed(speed)
              .build();
      if (calendar.getTimeInMillis() != currentDate.getTime()) {
        calendar.add(Calendar.HOUR, 1);
        totalMs += 60 * 60 * 1000;
      }
      currentDate = calendar.getTime();
      newTransportationPlan.add(transportationPlan);
      previousCity = city;

      if (n.getUbigeo().equals(previous.getCity().getUbigeo())) {
        break;
      }
    }

    int finalTotalMs = totalMs;
    remainingList.forEach(plan -> {
      plan.setId(null);
      calendar.setTime(plan.getRouteStart());
      calendar.add(Calendar.MILLISECOND, finalTotalMs);
      plan.setRouteStart(calendar.getTime());
      calendar.setTime(plan.getRouteFinish());
      calendar.add(Calendar.MILLISECOND, finalTotalMs);
      plan.setRouteFinish(calendar.getTime());
    });
    newTransportationPlan.addAll(remainingList);

    transportationPlanRepository.deleteAll(transportationPlanList);
    transportationPlanRepository.saveAll(newTransportationPlan);
  }

  private AlgorithmRequest constructAlgorithmRequest(TransportationPlan lastPlan, Truck truck, int packageAmount, List<RouteBlock> blockList) {
    Date currentDate = new Date();

    List<TruckAlgorithmRequest> truckAlgorithmList = new ArrayList<>();
      truckAlgorithmList.add(TruckAlgorithmRequest.builder()
              .id(truck.getId())
              .ubigeo(truck.getCurrentCity().getUbigeo())
              .maxLoad(truck.getStatus().equals(TruckStatus.AVAILABLE) ? truck.getCapacity() : getTruckLoad(truck))
              .build());

    List<OrderAlgorithmRequest> orderAlgorithmList = new ArrayList<>();
    orderAlgorithmList.add(OrderAlgorithmRequest.builder()
            .id(69420L)
            .remainingTime(72.0)
            .ubigeo(lastPlan.getCity().getUbigeo())
            .packages(packageAmount)
            .build());

    List<BlockadeAlgorithmRequest> blockadeAlgorithmList = new ArrayList<>();
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

  private Truck getNewTruck(List<Truck> truckList, int packageAmount) {
    List<Truck> available = truckList.stream().filter(t -> t.getStatus().equals(TruckStatus.AVAILABLE)).collect(Collectors.toList());
    if (available.isEmpty()) {
      available = truckList.stream()
              .filter(t -> t.getStatus().equals(TruckStatus.ONROUTE))
              .collect(Collectors.toList());
    }

    available.sort(Comparator.comparingInt(Truck::getCapacity));
    for (Truck t : available) {
      if (packageAmount < t.getCapacity()) {
        return t;
      }
    }

    return null;
  }

  private Integer getTruckLoad(Truck truck) {
    int capacity = 0;
    for (TransportationPlan t : truck.getTransportationPlanList()) {
      capacity += t.getAmount();
    }
    return capacity;
  }

  private void setTruckLocation(Breakdown breakdown, TransportationPlan previous, TransportationPlan current, Date currentDate) {
    double traveledFraction = (double) (currentDate.getTime() - current.getRouteStart().getTime()) /
            (double) (current.getRouteFinish().getTime() - current.getRouteStart().getTime());
    double longitude = previous.getCity().getLongitude();
    longitude += (current.getCity().getLongitude() - longitude) * traveledFraction;
    double latitude = previous.getCity().getLatitude();
    latitude += (current.getCity().getLatitude() - latitude) * traveledFraction;

    breakdown.setStopLatitude(latitude);
    breakdown.setStopLongitude(longitude);
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
}
