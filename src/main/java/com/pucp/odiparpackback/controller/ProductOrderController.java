package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.dto.ProductOrderDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productOrder")
public class ProductOrderController {
  private final ProductOrderService productOrderService;

  public ProductOrderController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @GetMapping
  public ResponseEntity<?> getAllProductOrders() {
    try {
      return ResponseEntity.ok(productOrderService.findAll());
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getProductOrders(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(productOrderService.findOne(id));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  //post method for create a new productOrder
  @PostMapping
  public ResponseEntity<?> createProductOrder(@RequestBody ProductOrderDto productOrder) {
    try {
      return ResponseEntity.ok(productOrderService.save(productOrder));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  //update method for update a productOrder
  @PostMapping
  public ResponseEntity<?> updateProductOrder(@RequestBody ProductOrderDto productOrder) {
    try {
      return ResponseEntity.ok(productOrderService.update(productOrder));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  //delete method for delete a productOrder
  @PostMapping("/{id}")
  public ResponseEntity<?> deleteProductOrder(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(productOrderService.delete(id));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
}