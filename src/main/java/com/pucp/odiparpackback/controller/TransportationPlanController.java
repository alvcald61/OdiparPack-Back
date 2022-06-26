package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.response.TransportationPlanResponse;
import com.pucp.odiparpackback.service.TransportationPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transportationPlan")
public class TransportationPlanController {
  @Autowired
  private TransportationPlanService transportationPlanService;

  @GetMapping("/list")
  public ResponseEntity<StandardResponse<List<TransportationPlanResponse>>> list() {
    StandardResponse<List<TransportationPlanResponse>> response = transportationPlanService.findAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("/create")
  public ResponseEntity<StandardResponse<Integer>> create() {
    StandardResponse<Integer> response = transportationPlanService.save();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping("/update")
  public ResponseEntity<StandardResponse<TransportationPlanResponse>> update() {
    StandardResponse<TransportationPlanResponse> response = transportationPlanService.update();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<StandardResponse<String>> deleteOne(@PathVariable Long id) {
    StandardResponse<String> response = transportationPlanService.delete();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping("/delete/all")
  public ResponseEntity<StandardResponse<String>> deleteAll() {
    StandardResponse<String> response = transportationPlanService.deleteAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
