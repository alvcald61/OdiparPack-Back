package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.request.ProductOrderRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductOrderService {

  final ProductOrderRepository productOrderRepository;

  final ObjectMapper objectMapper;


  public ProductOrderService(ProductOrderRepository productOrderRepository, ObjectMapper objectMapper) {
    this.productOrderRepository = productOrderRepository;
    this.objectMapper = objectMapper;
  }

  public List<ProductOrderRequest> findAll() {
    return productOrderRepository.findAll().stream().map(objectMapper::productOrderToDto).collect(Collectors.toList());
  }

  public ProductOrderRequest save(ProductOrderRequest productOrder) {
    return objectMapper.productOrderToDto(productOrderRepository.save(objectMapper.dtoToProductOrder(productOrder)));
  }

  public ProductOrderRequest update(ProductOrderRequest productOrder) {
    if (productOrder.getId() == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id del pedido no puede ser nulo");
    }
    ProductOrder productOrderDB = productOrderRepository.findById(productOrder.getId()).orElse(null);
    if (productOrderDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + productOrder.getId() + " no existe");
    }
    return objectMapper.productOrderToDto(productOrderRepository.save(objectMapper.dtoToProductOrder(productOrder)));
  }

  public boolean delete(Long id) {
    ProductOrder productOrder = productOrderRepository.findById(id).orElse(null);
    if (productOrder == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    productOrderRepository.delete(productOrder);
    return true;
  }

  public ProductOrderRequest findOne(Long id) {
    Optional<ProductOrder> order = productOrderRepository.findById(id);
    if (order.isEmpty()) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    return objectMapper.productOrderToDto(order.get());
  }
}
