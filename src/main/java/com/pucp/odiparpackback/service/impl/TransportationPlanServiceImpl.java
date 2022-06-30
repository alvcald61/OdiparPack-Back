package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.model.TransportationPlan;
import com.pucp.odiparpackback.repository.TransportationPlanRepository;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.response.*;
import com.pucp.odiparpackback.service.TransportationPlanService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import com.pucp.odiparpackback.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TransportationPlanServiceImpl implements TransportationPlanService {
  private final TransportationPlanRepository transportationPlanRepository;
  private final ObjectMapper objectMapper;

  @Override
  public StandardResponse<List<TransportationPlanResponse>> findAll() {
    try {
      List<TransportationPlan> transportationPlanList = transportationPlanRepository.findAll();
      List<TransportationPlanResponse> responseList = new ArrayList<>();
      for (TransportationPlan t : transportationPlanList) {
        ProductOrder po = t.getOrder();
        ProductOrderResponse productOrderResponse = createOrderResponse(po);

        TransportationPlanResponse transportationPlanResponse = TransportationPlanResponse.builder()
          .idTransportationPlan(t.getId())
          .order(productOrderResponse)
          .routeStart(TimeUtil.formatDate(t.getRouteStart()))
          .routeFinish(TimeUtil.formatDate(t.getRouteFinish()))
          .build();
        responseList.add(transportationPlanResponse);
      }
      return new StandardResponse<>(responseList);
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      return new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public StandardResponse<Integer> save() {
    return null;
  }

  @Override
  public StandardResponse<TransportationPlanResponse> update() {
    return null;
  }

  @Override
  public StandardResponse<String> delete() {
    return null;
  }

  @Override
  public StandardResponse<String> deleteAll() {
    return null;
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
}
