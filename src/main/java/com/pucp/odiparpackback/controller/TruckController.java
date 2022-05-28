package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.controller.json.ErrorJson;
import com.pucp.odiparpackback.controller.json.ResponseJson;
import com.pucp.odiparpackback.dto.TruckDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.TruckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/truck")
public class TruckController {

  private final TruckService truckService;

  public TruckController(TruckService truckService) {
    this.truckService = truckService;
  }

  @GetMapping
  public ResponseEntity<ResponseJson<List<TruckDto>>> getAllTrucks() {
    try {
      List<TruckDto> trucks = truckService.findAll();
      return ResponseEntity.ok(new ResponseJson<>(trucks));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @PostMapping
  public ResponseEntity<ResponseJson<TruckDto>> createTruck(@RequestBody TruckDto truckDto) {
    try {
      TruckDto truck = truckService.save(truckDto);
      return ResponseEntity.ok(new ResponseJson<>(truck));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }
}
