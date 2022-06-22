package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Client;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.ClientRepository;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.request.ProductOrderRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.response.CityResponse;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.ProductOrderResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.ProductOrderService;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.ObjectMapper;
import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

  @Autowired
  private ProductOrderRepository productOrderRepository;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private ClientRepository clientRepository;

  final ObjectMapper objectMapper;


  public ProductOrderServiceImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public StandardResponse<List<ProductOrderResponse>> findAll() {
    List<ProductOrder> productOrderList = productOrderRepository.findAll();
    List<ProductOrderResponse> productOrderResponseList = new ArrayList<>();
    for (ProductOrder po : productOrderList) {
      ClientResponse client = null;
      if (Objects.nonNull(po.getClient())) {
        client = ClientResponse.builder()
                .id(po.getClient().getId())
                .name(po.getClient().getName())
                .ruc(po.getClient().getRuc())
                .build();
      }
      City dest = po.getDestination();
      CityResponse city = CityResponse.builder()
              .id(dest.getId())
              .name(dest.getName())
              .ubigeo(dest.getUbigeo())
              .latitude(dest.getLatitude())
              .longitude(dest.getLongitude())
              .region(dest.getRegion().name())
              .build();
      productOrderResponseList.add(ProductOrderResponse.builder()
              .id(po.getId())
              .amount(po.getAmount())
              .maxDeliveryDate(TimeUtil.formatDate(po.getMaxDeliveryDate()))
              .registryDate(TimeUtil.formatDate(po.getRegistryDate()))
              .state(po.getState().name())
              .destination(city)
              .client(client)
              .build());
    }
    return new StandardResponse<>(productOrderResponseList);
  }

  @Override
  public StandardResponse<Long> save(ProductOrderRequest request) {
    StandardResponse<Long> response;
    if (Objects.isNull(request.getDestinationUbigeo())) {
      ErrorResponse error = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "destinationId"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
      return response;
    }

    try {
      City city = cityRepository.findByUbigeo(request.getDestinationUbigeo());
      Client client = clientRepository.findByRuc(request.getClientRuc()).orElse(null);
      if (Objects.isNull(client)) {
        ErrorResponse error = new ErrorResponse(String.format(Message.FIELD_NOT_FOUND, "cliente", request.getClientRuc()));
        response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
        return response;
      }

      if (Objects.isNull(city)) {
        ErrorResponse error = new ErrorResponse(String.format(Message.FIELD_NOT_FOUND, "ciudad", request.getDestinationUbigeo()));
        response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
        return response;
      }
      Date date = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.add(Calendar.DAY_OF_MONTH, (int) (city.getRegion().getMaxHours()) / 24);

      ProductOrder po = ProductOrder.builder()
              .registryDate(date)
              .maxDeliveryDate(c.getTime())
              .destination(city)
              .state(OrderState.PROCESSING)
              .client(client)
              .amount(request.getAmount()).build();
      Long orderId = productOrderRepository.save(po).getId();
      response = new StandardResponse<>(orderId);
    } catch (Exception e) {
      ErrorResponse errorResponse = new ErrorResponse(Message.INSERT_ERROR);
      response = new StandardResponse<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public StandardResponse<Long> update(ProductOrderRequest request) {
    StandardResponse<Long> response;
    if (request.getOrderId() == null) {
      ErrorResponse errorResponse = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "orderId"));
      return new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    ProductOrder productOrder = productOrderRepository.findById(request.getOrderId()).orElse(null);
    if (productOrder == null) {
      ErrorResponse errorResponse = new ErrorResponse("El pedido con id " + request.getOrderId() + " no existe");
      return new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    Long orderId = productOrderRepository.save(productOrder).getId();
    response = new StandardResponse<>(orderId);
    return response;
  }

  @Override
  public StandardResponse<Boolean> delete(Long id) {
    ProductOrder productOrder = productOrderRepository.findById(id).orElse(null);
    if (productOrder == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    productOrderRepository.delete(productOrder);
    return new StandardResponse<>(true);
  }

  @Override
  public StandardResponse<ProductOrderResponse> findOne(Long id) {
    StandardResponse<ProductOrderResponse> response;
    Optional<ProductOrder> order = productOrderRepository.findById(id);
    if (order.isEmpty()) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    try {
      ProductOrder po = productOrderRepository.findById(id).orElse(null);
      if (Objects.isNull(po)) {
        ErrorResponse errorResponse = new ErrorResponse(Message.RESOURCE_NOT_FOUND);
        response = new StandardResponse<>(errorResponse, HttpStatus.NOT_FOUND);
        return response;
      }

      Client c = po.getClient();
      ProductOrderResponse orderResponse = ProductOrderResponse.builder()
              .id(po.getId())
              .state(po.getState().name())
              .registryDate(TimeUtil.formatDate(po.getRegistryDate()))
              .maxDeliveryDate(TimeUtil.formatDate(po.getMaxDeliveryDate()))
              .amount(po.getAmount())
              .client(Objects.nonNull(c) ? ClientResponse.builder().id(c.getId()).name(c.getName()).ruc(c.getRuc()).build() : null)
              .build();
      response = new StandardResponse<>(orderResponse);
    } catch (Exception ex) {
      ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }
}
