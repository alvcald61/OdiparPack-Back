package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/city")
public class CityController {
  
  private final CityService cityService;

  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @GetMapping
  public ResponseEntity<?> getAllCities() {
    try {
      return ResponseEntity.ok(cityService.findAll());
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  @PostMapping
  public ResponseEntity<?> createCity(@RequestBody City city) {
    try {
      return ResponseEntity.ok(cityService.save(city));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  @PutMapping
  public ResponseEntity<?> updateCity(@RequestBody City city) {
    try {
      return ResponseEntity.ok(cityService.update(city));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCity(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(cityService.delete(id));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
  
  
  
}
