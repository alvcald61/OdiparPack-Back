package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Client;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.request.ProductOrderRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

  @Autowired
  private  ProductOrderRepository productOrderRepository;

  final ObjectMapper objectMapper;


  public ProductOrderServiceImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public StandardResponse<List<ProductOrderResponse>> findAll() {
    List<ProductOrder> productOrderList = productOrderRepository.findAll();
    List<ProductOrderResponse> productOrderResponseList = new ArrayList<>();
    for (ProductOrder po : productOrderList) {
      productOrderResponseList.add(ProductOrderResponse.builder().id(po.getId()).amount(po.getAmount())
              .maxDeliveryDate(po.getMaxDeliveryDate().toString()).registryDate(po.getRegistryDate().toString())
              .state(po.getState().name()).build());
    }
    return new StandardResponse<>(productOrderResponseList);
  }

  @Override
  public StandardResponse<Long> save(ProductOrderRequest request) {
    StandardResponse<Long> response;
    if (Objects.isNull(request.getDestinationId())) {
      ErrorResponse error = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "destinationId"));
      response = new StandardResponse<>(error,HttpStatus.BAD_REQUEST);
      return response;
    }

    try {
      City city = City.builder().id(request.getDestinationId()).build();
      Client client = null;
      if (Objects.nonNull(request.getClientId())) {
        client = Client.builder().id(request.getClientId()).build();
      }

      ProductOrder po = ProductOrder.builder()
              .registryDate(TimeUtil.parseDate(request.getRegistryDate()))
              .maxDeliveryDate(TimeUtil.parseDate(request.getMaxDeliveryDate()))
              .destination(city)
              .state(OrderState.valueOf(request.getState()))
              .client(client)
              .amount(request.getAmount()).build();
      Long orderId = productOrderRepository.save(po).getId();
      response = new StandardResponse<>(orderId);
    } catch (ParseException e) {
      ErrorResponse errorResponse = new ErrorResponse("Error al procesar la fecha");
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
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
    try {
      productOrder.setState(OrderState.valueOf(request.getState()));
      productOrder.setRegistryDate(TimeUtil.parseDate(request.getRegistryDate()));
      Long orderId = productOrderRepository.save(productOrder).getId();
      response = new StandardResponse<>(orderId);
    } catch (ParseException e) {
      ErrorResponse errorResponse = new ErrorResponse("Error al procesar la fecha");
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
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
