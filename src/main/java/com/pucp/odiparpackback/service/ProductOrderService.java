package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderService {
  final ProductOrderRepository productOrderRepository;

  public ProductOrderService(ProductOrderRepository productOrderRepository) {
    this.productOrderRepository = productOrderRepository;
  }

  public List<ProductOrder> findAll() {
    return productOrderRepository.findAll();
  }

  public ProductOrder save(ProductOrder productOrder) {
    return productOrderRepository.save(productOrder);
  }

  public ProductOrder update(ProductOrder productOrder) {
    if(productOrder.getId() == null) {
     throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El id del pedido no puede ser nulo");
    }
    ProductOrder productOrderDB = productOrderRepository.findById(productOrder.getId()).orElse(null);
    if(productOrderDB == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + productOrder.getId() + " no existe");
    }
    return productOrderRepository.save(productOrder);
  }

  public ProductOrder delete(Long id) {
    ProductOrder productOrder = productOrderRepository.findById(id).orElse(null);
    if(productOrder == null) {
      throw new GenericCustomException(HttpStatus.BAD_REQUEST, "El pedido con id " + id + " no existe");
    }
    productOrderRepository.delete(productOrder);
    return productOrder;
  }

  public ProductOrder findOne(Long id) {
    return productOrderRepository.findById(id).orElse(null);
  }
}
