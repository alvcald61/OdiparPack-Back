package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
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

  @GetMapping
  public ResponseEntity<StandardResponse<List<TruckRequest>>> getAllTrucks() {
    try {
      List<TruckRequest> trucks = truckService.findAll();
      return ResponseEntity.ok(new StandardResponse<>(trucks));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
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
