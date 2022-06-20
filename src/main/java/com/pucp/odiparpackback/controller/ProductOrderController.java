package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.request.ProductOrderRequest;
import com.pucp.odiparpackback.response.ProductOrderResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productOrder")
public class ProductOrderController {

  @Autowired
  private ProductOrderService productOrderService;

  @GetMapping("/list")
  public ResponseEntity<StandardResponse<List<ProductOrderResponse>>> getAllProductOrders() {
    StandardResponse<List<ProductOrderResponse>> response = productOrderService.findAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/list/{id}")
  public ResponseEntity<StandardResponse<ProductOrderResponse>> getProductOrders(@PathVariable Long id) {
    StandardResponse<ProductOrderResponse> response = productOrderService.findOne(id);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  //post method for create a new productOrder
  @PostMapping("/create")
  public ResponseEntity<StandardResponse<Long>> createProductOrder(@RequestBody ProductOrderRequest productOrder) {
    StandardResponse<Long> response = productOrderService.save(productOrder);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  //update method for update a productOrder
  @PutMapping("/update")
  public ResponseEntity<StandardResponse<Long>> updateProductOrder(@RequestBody ProductOrderRequest productOrder) {
    StandardResponse<Long> response = productOrderService.update(productOrder);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  //delete method for delete a productOrder
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<StandardResponse<Boolean>> deleteProductOrder(@PathVariable Long id) {
    StandardResponse<Boolean> response = productOrderService.delete(id);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
