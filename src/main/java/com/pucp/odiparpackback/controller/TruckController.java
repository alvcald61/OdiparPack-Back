package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.response.TruckResponse;
import com.pucp.odiparpackback.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/truck")
public class TruckController {

  @Autowired
  private TruckService truckService;

  @GetMapping("/simulation")
  public ResponseEntity<StandardResponse<List<TruckRequest>>> getAllTrucksSimulation() {
    StandardResponse<List<TruckRequest>> response = truckService.findAllSimulation();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
  @GetMapping("/findAll")
  public ResponseEntity<StandardResponse<List<TruckResponse>>> getAllTrucks() {
    StandardResponse<List<TruckResponse>> response = truckService.findAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<StandardResponse<TruckResponse>> findOne(@PathVariable Long id) {
    StandardResponse<TruckResponse> response = truckService.findOne(id);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping
  public ResponseEntity<StandardResponse<TruckRequest>> createTruck(@RequestBody TruckRequest truckDto) {
    try {
      TruckRequest truck = truckService.save(truckDto);
      return ResponseEntity.ok(new StandardResponse<>(truck));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }
}
