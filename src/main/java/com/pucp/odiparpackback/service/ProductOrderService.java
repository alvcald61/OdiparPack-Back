package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.dto.ProductOrderDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductOrderService {
  
  final ProductOrderRepository productOrderRepository;
  


  public ProductOrderService(ProductOrderRepository productOrderRepository) {
    this.productOrderRepository = productOrderRepository;
  }

  public List<ProductOrderDto> findAll() {
    return productOrderRepository.findAll().stream().map(ObjectMapper::productOrderToDto).collect(Collectors.toList());
  }

  public ProductOrderDto save(ProductOrderDto productOrder) {
    return ObjectMapper.productOrderToDto(productOrderRepository.save(ObjectMapper.dtoToProductOrder(productOrder)));
  }

  public ProductOrderDto update(ProductOrderDto productOrder) {
    if(productOrder.getId() == null) {
     throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id del pedido no puede ser nulo");
    }
    ProductOrder productOrderDB = productOrderRepository.findById(productOrder.getId()).orElse(null);
    if(productOrderDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + productOrder.getId() + " no existe");
    }
    return ObjectMapper.productOrderToDto(productOrderRepository.save(ObjectMapper.dtoToProductOrder(productOrder)));
  }

  public boolean delete(Long id) {
    ProductOrder productOrder = productOrderRepository.findById(id).orElse(null);
    if(productOrder == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    productOrderRepository.delete(productOrder);
    return true;
  }

  public ProductOrderDto findOne(Long id) {
    Optional<ProductOrder> order =  productOrderRepository.findById(id);
    if(order.isEmpty()) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    return ObjectMapper.productOrderToDto(order.get());
  }
}
